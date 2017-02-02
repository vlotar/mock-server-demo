package com.vlotar.demo.mockserver.clients;

import com.vlotar.demo.mockserver.domain.User;
import com.vlotar.demo.mockserver.error.UserClientErrorDecoder;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import static com.vlotar.demo.mockserver.clients.UserClient.USER_SERCIVE_URL;

/**
 * This class contains all relevant for user resource business logic
 *
 * @author vlotar
 * @version 1.0
 * @since 1.0
 */
public class UserLogic {

	private final UserClient userClient;

	public UserLogic() {
		//initialize user client to execute authentication
		this.userClient = Feign.builder()
				.errorDecoder(new UserClientErrorDecoder())
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(UserClient.class, USER_SERCIVE_URL);

	}

	public User findUser(final Long userId) {
		return this.userClient.getUserById(userId);
	}

	public User findUserByEmail(final String email) {
		return this.userClient.getUserByEmail(email);
	}

	public Long createUser(final String firstName, final String lastName, final String email) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		return this.userClient.createUser(user).getUserId();
	}
}
