package com.xoriant.bank.model;

public enum ErrorCode {

	NEW_BRANCH_ADDITION_FAILED("ErrorCode - Failed While Adding new Branch ", 1000),
	EXISTING_BRANCH_UPDATION_FAILED("ErrorCode - Failed while updating the existing branch ",1001),
	PUBLISHING_MSG_TO_QUEUE_FAILED("ErrorCode - Failed while publishing message to queue ",1002),
	DELETION_OF_BRANCH_FAILED("Error code - Failed while deleting existing branch",1003);

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
