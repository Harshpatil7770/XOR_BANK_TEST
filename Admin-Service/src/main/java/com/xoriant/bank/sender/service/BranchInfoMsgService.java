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
public class BranchInfoMsgService {

	private final Logger logger=LoggerFactory.getLogger(BranchInfoMsgService.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${branchOutQueue}")
	private String outQueue;

	@Bean
	public Queue branchDetailsQueue() {
		return new Queue(outQueue, false);
	}

	@Transactional
	public <T extends Serializable> void publishMessageToQueue(final T message) throws Exception{
		logger.info("Sending msg to the queue >>> {} ",outQueue);
		long startTime = System.currentTimeMillis();
		rabbitTemplate.convertAndSend(outQueue, message);
		logger.info("Time Required to put msg over the queue {} ms",System.currentTimeMillis()-startTime);
		logger.info("Message sent succesfully to the queue.");
	}

}
