package com.vlotar.demo.mockserver.clients;

import com.vlotar.demo.mockserver.domain.User;
import com.vlotar.demo.mockserver.error.UserClientException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import static com.vlotar.demo.mockserver.clients.UserClient.USERS_RESOURCE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

/**
 * MockServer allows to validate the request which is sent my the system under testing and mock the response
 * in order to check how the system processes it.
 * It also allows to isolate the system under testing.
 *
 * @author vlotar
 */
public class UserLogicTest {

	private ClientAndServer mockServer;

	@Before
	public void startMockServer() {
		this.mockServer = startClientAndServer(8888);
	}

	@After
	public void stopMockServer() {
		mockServer.stop();
	}

	@Test
	public void testFindUser() throws Exception {
		this.mockServer
				.when(
						request()
								.withMethod("GET")
								.withPath("/user-service" + USERS_RESOURCE + "/1"),
						exactly(1))
				.respond(
						response()
								.withStatusCode(200)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{\n \"userId\": \"1\", \"firstName\": \"Foo\",\n  \"lastName\": \"Bar\",\n  \"email\": \"foo.bar@example.com\"\n}")
								.withDelay(new Delay(SECONDS, 1))
				);

		//user real implementation
		User user = new UserLogic().findUser(1L);

		Assert.assertEquals("Foo", user.getFirstName());
		Assert.assertEquals("Bar", user.getLastName());
		Assert.assertEquals("foo.bar@example.com", user.getEmail());
	}

	@Test
	public void testFindUser_ERROR() throws Exception {
		this.mockServer
				.when(
						request()
								.withMethod("GET")
								.withPath("/user-service" + USERS_RESOURCE + "/2"),
						exactly(1))
				.respond(
						response()
								.withStatusCode(404)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{\n \"errorCode\": \"1001\", \"errorMessage\": \"User with the given identifier cannot be found\"}")
								.withDelay(new Delay(SECONDS, 1))
				);

		//user real implementation
		try {
			new UserLogic().findUser(2L);
			Assert.fail("A specific exception should occur");
		} catch (UserClientException e) {
			Assert.assertEquals(404, e.getHttpStatus());
			Assert.assertEquals("Not Found", e.getReason());
			Assert.assertEquals("User with the given identifier cannot be found", e.getResponse().getErrorMessage());
			Assert.assertEquals("1001", e.getResponse().getErrorCode());
		}
	}

	@Test
	public void testFindUserByEmail() throws Exception {
		this.mockServer
				.when(
						request()
								.withMethod("GET")
								.withPath("/user-service" + USERS_RESOURCE)
								.withQueryStringParameter("email", "foo.bar@example.com"),
						exactly(1))
				.respond(
						response()
								.withStatusCode(200)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{\n \"userId\": \"1\", \"firstName\": \"Foo\",\n  \"lastName\": \"Bar\",\n  \"email\": \"foo.bar@example.com\"\n}")
								.withDelay(new Delay(SECONDS, 1))
				);

		//user real implementation
		User user = new UserLogic().findUserByEmail("foo.bar@example.com");

		Assert.assertEquals("Foo", user.getFirstName());
		Assert.assertEquals("Bar", user.getLastName());
		Assert.assertEquals("foo.bar@example.com", user.getEmail());
	}

	@Test
	public void testCreateUser() throws Exception {
		this.mockServer
				.when(
						request()
								.withMethod("POST")
								.withPath("/user-service" + USERS_RESOURCE)
								.withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
								.withBody(exact("{\n  \"firstName\": \"Foo\",\n  \"lastName\": \"Bar\",\n  \"email\": \"foo.bar@example.com\"\n}")),
						exactly(1))
				.respond(
						response()
								.withStatusCode(201)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{\"userId\": \"1\"}")
								.withDelay(new Delay(SECONDS, 1))
				);

		//user real implementation
		Long userId = new UserLogic().createUser("Foo", "Bar", "foo.bar@example.com");
		//check the result
		Assert.assertEquals(new Long(1), userId);
	}

}