package com.vlotar.demo.mockserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Basic exception which has to be thrown
 * if there's any error occurred while talking to the client service.
 *
 * @author vlotar
 */
@AllArgsConstructor
@Getter
public class UserClientException extends RuntimeException {

	private final int httpStatus;

	private final String reason;

	private final ErrorResponse response;
}
