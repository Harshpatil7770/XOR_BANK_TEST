package com.xoriant.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

	@Bean
	public void loadModuleInfo() {

		System.out.println("************************************************************");
		System.out.println("************************************************************");
		System.out.println("************************************************************");
		System.out.println("********************** Eureka Server Started ***************");
		System.out.println("************************************************************");
		System.out.println("************************************************************");
		System.out.println("************************************************************");
	}

}
