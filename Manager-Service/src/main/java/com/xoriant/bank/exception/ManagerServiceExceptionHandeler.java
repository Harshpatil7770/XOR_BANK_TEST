package com.xoriant.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ManagerServiceExceptionHandeler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DateOfBirthException.class)
	public ResponseEntity<String> dateOfBirthExceptionHandeler(DateOfBirthException exception) {
		return new ResponseEntity<>("Please enter past date......!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccountTypeExpection.class)
	public ResponseEntity<String> accountTypeException(AccountTypeExpection expection) {
		return new ResponseEntity<>("Please check the account type....!", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> elementNotFoundException(ElementNotFoundException exception) {
		return new ResponseEntity<>("Element Not Present in the database ....!", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> customerAddressNotFound(CustomerAddressException addressException) {
		return new ResponseEntity<>("Customer Address Not Found in database .....!", HttpStatus.BAD_REQUEST);
	}

//	public ResponseEntity<String> customerNotFoundException(CustomerPresentException customerException) {
//		return new ResponseEntity<>("Customer not present in database....!", HttpStatus.BAD_REQUEST);
//	}
}
