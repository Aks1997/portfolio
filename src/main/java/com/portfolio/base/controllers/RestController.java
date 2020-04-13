package com.portfolio.base.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.ResponseData;

@RestControllerAdvice
public class RestController {

	@ExceptionHandler(value = BaseException.class)
	public ResponseEntity<ResponseData<Object>> handleBaseException(BaseException exception){
		HttpStatus status;
		if(exception.getErrorCode()!=null) {
			status=exception.getErrorCode();
		}else {
			status= HttpStatus.BAD_REQUEST;
		}
		ResponseData<Object> responseData= new ResponseData<Object>(null, exception.getMessage(), true);
		return new ResponseEntity<ResponseData<Object>>(responseData, status);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseData<Object>> handleGenericException(Exception exception){
		ResponseData<Object> responseData= new ResponseData<Object>(null, exception.getMessage(), true);
		return new ResponseEntity<ResponseData<Object>>(responseData, HttpStatus.BAD_REQUEST);
	}
}
