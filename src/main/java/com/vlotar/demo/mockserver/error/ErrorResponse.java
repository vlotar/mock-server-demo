package com.vlotar.demo.mockserver.error;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Basic model for an error response which can be received from the 3rd party service side.
 *
 * @author vlotar
 * @version 1.0
 */
@Getter
@Setter
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = -6986639796860620250L;

	private String errorCode;

	private String errorMessage;
}
