package com.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Service Unavailable Temporarily")
public class ServiceUnavailableException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
