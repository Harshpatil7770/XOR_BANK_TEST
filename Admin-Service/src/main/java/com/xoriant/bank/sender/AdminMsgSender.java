package com.xoriant.bank.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminMsgSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	public Queue branchDetailsQueue() {
		return new Queue("XOR.BRANCH.INFO.QUEUE", false);
	}

	public void addNewBranchDetails(String branchDetails) {
		log.info("Sending msg to the queue ..." + branchDetails);
		rabbitTemplate.convertAndSend("XOR.BRANCH.INFO.QUEUE", branchDetails);
		log.info("Message sent succesfully to the queue");
	}

	public void updateBranchDetails(String updateBranchDetails) {
		log.info("Sending msg to the queue ....." + updateBranchDetails);
		rabbitTemplate.convertAndSend("XOR.BRANCH.INFO.QUEUE", updateBranchDetails);
		log.info("Message sent succesfully to the queue");
	}

	@Bean
	public Queue managerRelatedDetailsQ() {
		return new Queue("XOR.BRANCH.MAN.QUEUE", false);
	}

	public void addNewManager(String newManagerDetails) {
		log.info("Sending msg to the queue ...." + newManagerDetails);
		rabbitTemplate.convertAndSend("XOR.BRANCH.MAN.QUEUE", newManagerDetails);
		log.info("Message sent succesfully to the queue");
	}

}
