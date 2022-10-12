package com.xoriant.bank.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ShutDownManager {

	@Autowired
	private ApplicationContext applicationContext;
	
	public void initiateShutDowm(int returnCode) {
		try {
		SpringApplication.exit(applicationContext,()-> returnCode);	
		}catch(Exception e) {
			
		}
	}
}
