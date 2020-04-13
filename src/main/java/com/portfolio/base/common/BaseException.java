package com.portfolio.base.common;

import org.springframework.http.HttpStatus;

public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;
	private HttpStatus errorCode;
	private String message;
	
	public BaseException(String message) {
		this.message= message;
	}
	
	public BaseException(HttpStatus errorCode, String message) {
		this.errorCode= errorCode;
		this.message= message;
	}
	
	public HttpStatus getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
