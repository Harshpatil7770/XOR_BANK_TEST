package com.xoriant.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
		System.out.println("***********************************************************************");
		System.out.println("***********************************************************************");
		System.out.println("***********************************************************************");
		System.out.println("*************************** Config Server Started *********************");
		System.out.println("***********************************************************************");
		System.out.println("***********************************************************************");
		System.out.println("***********************************************************************");

	}

}
