package com.xoriant.bank.sender.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManagerInfoMsgSender {

	Logger logger = LoggerFactory.getLogger(ManagerInfoMsgSender.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${managerOutQueue}")
	private String managerOutQueue;

	@Bean
	public Queue managerDetails() {
		return new Queue(managerOutQueue, false);
	}

	// while sending msg to queue it must be final
	@Transactional
	public <T extends Serializable> void managerDetails(final T msg) throws Exception{

			logger.info("Manager Queue Name >>> {} ", managerOutQueue);
			long startTime = System.currentTimeMillis();
			rabbitTemplate.convertAndSend(managerOutQueue, msg);
			logger.info("Sent message to queue succesfully");
			logger.info("Time taken to put message into queue {} millseconds ", System.currentTimeMillis()-startTime);
			logger.info(" *********** Succesfully published message to Manager Queue ***********");

	}

}
