package com.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author iamsagarvarma
 * @since 08-oct-2017
 *
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomException() {
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}
}
