package com.xoriant.bank.service;

public interface ErrorCommond {

	public void loadError(int errorCode, String description);

	public void processError();
}
