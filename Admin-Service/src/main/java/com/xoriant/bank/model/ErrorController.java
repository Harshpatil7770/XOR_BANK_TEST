package com.xoriant.bank.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xoriant.bank.service.ErrorCommond;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorController {

	@Autowired
	private ShutDownCmd shutDownCmd;

	public void handleErrorCode(ErrorCode errorCode) {
		switch (errorCode) {
		case NEW_BRANCH_ADDITION_FAILED: {
			log.error("ErrorController - Error occured while adding new branch");
			processShutDownCmd(errorCode);
			break;
		}
		default:
			log.info("No Handler is found " + errorCode.getErrorCode() + " " + errorCode.getDescription());
		}
	}

	private void processShutDownCmd(ErrorCode errorCode) {
		shutDownCmd.loadError(errorCode.getErrorCode(), errorCode.getDescription());
		shutDownCmd.processError();
	}
}
