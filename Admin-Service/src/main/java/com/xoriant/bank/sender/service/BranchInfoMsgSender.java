package com.xoriant.bank.sender.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.xoriant.bank.model.Branch;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BranchInfoMsgSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	public Queue branchDetailsQueue() {
		return new Queue("XOR.BRANCH.INFO.QUEUE", false);
	}

	public void publishMessageToQueue(String message) {
		log.info("Sending msg to the queue ..." + message);
		long startTime = System.currentTimeMillis();
		rabbitTemplate.convertAndSend("XOR.BRANCH.INFO.QUEUE",message);
		long endTime = System.currentTimeMillis();
		long actualRequiredTime = endTime - startTime;
		log.info("Time Required to put msg over the queue " + actualRequiredTime + " milliseconds");
		log.info("Message sent succesfully to the queue");
	}

}
