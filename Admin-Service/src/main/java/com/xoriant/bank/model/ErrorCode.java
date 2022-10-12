package com.xoriant.bank.model;

public enum ErrorCode {

	NEW_BRANCH_ADDITION_FAILED("Error - Failed While Adding new Branch ",1000);
	
	private String description;
	
	private int errorCode;

	private ErrorCode(String description, int errorCode) {
		this.description = description;
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	
	
}
