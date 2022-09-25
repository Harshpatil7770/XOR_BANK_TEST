package com.xoriant.bank.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.xoriant.bank.model.Branch;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginMsgSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Bean
	public Queue BranchDetailsQueue() {
		return new Queue("BranchDetailsQ",false);
	}
	
	public void addNewBranchDetails(String branchDetails) {
		log.info("Sending msg to the queue ..."+branchDetails);
		rabbitTemplate.convertAndSend("BranchDetailsQ", branchDetails);
		log.info("Message sent succesfully to the queue");
	}
	
	public void updateBranchDetails(String updateBranchDetails) {
		log.info("Sending msg to the queue ....."+updateBranchDetails);
		rabbitTemplate.convertAndSend("updareBranchDetalilsQ",updateBranchDetails);
		log.info("Message sent succesfully to the queue");
	}
	
}
