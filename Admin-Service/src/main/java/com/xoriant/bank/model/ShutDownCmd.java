package com.xoriant.bank.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xoriant.bank.service.ErrorCommond;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShutDownCmd implements ErrorCommond {

	private int errorCode = 0;

	private String description = null;

	@Autowired
	ShutDownManager shutDownManager;

	
	@Override
	public void loadError(int errorCode, String description) {
		this.errorCode = errorCode;
		this.description = description;
	}

	@Override
	public void processError() {
		log.error("**************************************************************************************");
		log.error("************************** Admin-Service Is Shutting Down ****************************");
		log.error("****** errorCode " + errorCode + " description - " + description);
		shutDownManager.initiateShutDowm(errorCode);
		log.error("************************* ADMIN_SERVICE SHUTDOWN COMPLETED **********************");
		System.exit(0);
	}

}
