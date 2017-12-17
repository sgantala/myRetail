package com.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Product id in request header and request body doesn't match")
public class ProductMisMatchException extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
}