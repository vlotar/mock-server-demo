package com.vlotar.demo.mockserver.clients;

import com.vlotar.demo.mockserver.domain.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * That's a very simple client of the User service which exposes RESTful API
 *
 * @author vlotar
 * @version 1.0
 * @since 1.0
 */
public interface UserClient {

	String USER_SERCIVE_URL = "http://localhost:8888/user-service";

	String USERS_RESOURCE = "/rest/v1/users";

	@RequestLine("POST " + USERS_RESOURCE)
	@Headers("Content-Type: application/json; charset=utf-8")
	User createUser(final User user);

	@RequestLine("GET " + USERS_RESOURCE + "/{userId}")
	User getUserById(final @Param("userId") Long userId);

	@RequestLine("GET " + USERS_RESOURCE + "?email={email}")
	User getUserByEmail(final @Param("email") String email);

}
