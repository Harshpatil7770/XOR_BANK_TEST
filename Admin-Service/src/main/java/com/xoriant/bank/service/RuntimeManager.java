package com.xoriant.bank.service;

import com.xoriant.bank.model.ErrorCode;

public interface RuntimeManager {

	public void getErrorController(ErrorCode errorCode);
}
