package com.vlotar.demo.mockserver.error;

import com.google.gson.Gson;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static feign.FeignException.errorStatus;

/**
 * Error handling mechanism for the Feign client of the User Service.
 * By default Feign only throws FeignException for any error situation.
 *
 * @author vlotar
 */
@Slf4j
public class UserClientErrorDecoder implements ErrorDecoder {

	private final Gson gson = new Gson();

	public Exception decode(String methodKey, Response response) {
		final String responseBody = extractResponseBody(response);
		//you can react differently on a client and on a server exception
		if (response.status() >= 400 && response.status() <= 499) {
			return new UserClientException(
					response.status(),
					response.reason(),
					this.gson.fromJson(responseBody, ErrorResponse.class)
			);
		}
		if (response.status() >= 500 && response.status() <= 599) {
			return new UserClientException(
					response.status(),
					response.reason(),
					this.gson.fromJson(responseBody, ErrorResponse.class)
			);
		}
		return errorStatus(methodKey, response);
	}

	private String extractResponseBody(final Response response) {
		String responseBody = "{}";
		try {
			responseBody = IOUtils.toString(response.body().asInputStream());
		} catch (IOException e) {
			log.error("Unexpected error occurred while parsing the response", e);
		}
		return responseBody;
	}
}
