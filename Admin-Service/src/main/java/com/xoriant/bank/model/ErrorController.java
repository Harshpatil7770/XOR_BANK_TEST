package com.xoriant.bank.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xoriant.bank.service.RuntimeManager;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorController implements RuntimeManager {

	@Autowired
	private ShutDownCmd shutDownCmd;

	public void getErrorController(ErrorCode errorCode) {
		switch (errorCode) {
		case NEW_BRANCH_ADDITION_FAILED: {
			log.error("ErrorController - Error occured while adding new branch");
			processShutDownCmd(errorCode);
			break;
		}
		case EXISTING_BRANCH_UPDATION_FAILED: {
			log.error("ErrorController - Error occured while updating existing branch");
			processShutDownCmd(errorCode);
			break;
		}
		case PUBLISHING_MSG_TO_QUEUE_FAILED: {
			log.error("ErrorController- Error occured while publishing message to queue");
			processShutDownCmd(errorCode);
			break;
		}
		case NEW_MANAGER_ADDITION_FAILED: {
			log.error("ErrorController- Error occured while adding new manager details");
			processShutDownCmd(errorCode);
			break;
		}
		case FAILED_FETCHING_MANAGER_DETAILS: {
			log.error("ErrorController - Error occured while fetching data from database");
			processShutDownCmd(errorCode);
			break;
		}
		case FAILED_UPDATING_THE_DETAILS: {
			log.error("ErrorController - Error occured while updating the details");
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
