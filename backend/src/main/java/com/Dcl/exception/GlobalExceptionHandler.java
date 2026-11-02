package com.Dcl.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Dcl.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

	//our own business errors (not found, conflict...) - just a warning
	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> handleAppException(AppException exception){
		logger.warn("AppException: {} ({})", exception.getMessage(), exception.getHttpStatus());
		ApiResponse response=new ApiResponse<>(exception.getMessage(), null,exception.getHttpStatus());
		return new ResponseEntity<>(response, exception.getHttpStatus());
	}

	//any unexpected error (DB down, null pointer...) - log full stack trace
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception exception){
		logger.error("Unexpected error: {}", exception.getMessage(), exception);
		ApiResponse response=new ApiResponse<>("Something went wrong! Please try again.", null,HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
