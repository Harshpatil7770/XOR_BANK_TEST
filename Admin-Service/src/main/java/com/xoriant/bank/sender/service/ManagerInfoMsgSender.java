package com.xoriant.bank.sender.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ManagerInfoMsgSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${managerOutQueue}")
	private String managerOutQueue;
	
	@Bean
	public Queue managerDetails() {
		return new Queue(managerOutQueue, false);
	}
	
	@Transactional
	public void managerDetails(String managerInfo)  {
		log.info("Sending message to queue....");
		long startTime=System.currentTimeMillis();
		rabbitTemplate.convertAndSend(managerOutQueue, managerInfo);
		long endTime=System.currentTimeMillis();
		log.info("Sent message to queue succesfully");
		long requiredTime=endTime-startTime;
		System.out.println("Total time required to send msg to queue is :: "+requiredTime+" milliseconds");
	}
	
}
