package com.xoriant.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xoriant.bank.util.ApplicationConstant;


@ControllerAdvice
public class GlobalExceptionHandeler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<String> unauthorizedExceptionHandeler(UnAuthorizedException authorizedException) {
		return new ResponseEntity<>(ApplicationConstant.UNAUTHORIZED_USER, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ElementNotFoundException.class)
	public ResponseEntity<String> elementNotFoundExceptionHandeler(ElementNotFoundException elementNotFoundException) {
		return new ResponseEntity<>(ApplicationConstant.ELEMENT_NOT_FOUND, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InputUserException.class)
	public ResponseEntity<String> inputUserException(InputUserException inputUserException) {
		return new ResponseEntity<>(ApplicationConstant.INPUT_USER_MISTAKE, HttpStatus.BAD_REQUEST);
	}
}
