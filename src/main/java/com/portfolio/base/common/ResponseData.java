package com.portfolio.base.common;

public class ResponseData<T> {
	
	private T data;
	private String message;
	private boolean error;
	
	public ResponseData() {
		// TODO Auto-generated constructor stub
	}
	
	public ResponseData(T data, String message, boolean error) {
		// TODO Auto-generated constructor stub
		this.data=data;
		this.message=message;
		this.error=error;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
