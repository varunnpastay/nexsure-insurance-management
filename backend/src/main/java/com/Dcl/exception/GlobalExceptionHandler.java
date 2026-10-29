package com.Dcl.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Dcl.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> handleAppException(AppException exception){
		ApiResponse response=new ApiResponse<>(exception.getMessage(), null,exception.getHttpStatus());
		return new ResponseEntity<>(response, exception.getHttpStatus());
	}
	
}
