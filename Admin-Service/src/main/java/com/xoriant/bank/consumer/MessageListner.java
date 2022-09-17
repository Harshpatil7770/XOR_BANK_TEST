package com.xoriant.bank.consumer;

import org.springframework.kafka.annotation.KafkaListener;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.util.KafkaConstant;

//@Controller
public class MessageListner {

	@KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	private void updateManagerDetails(ManagerDTO managerDTO) {
		Manager manager = new Manager();
		manager.setManagerId(managerDTO.getManagerId());
		manager.setFirstName(managerDTO.getFirstName());
		manager.setLastName(managerDTO.getLastName());
		manager.setUserName(managerDTO.getUserName());
		manager.setPassword(managerDTO.getPassword());
		manager.setUserType(managerDTO.getUserType());
		Address address = new Address();
		address.setAddressId(managerDTO.getAddressDTO().getAddressId());
		address.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
		address.setHouseName(managerDTO.getAddressDTO().getHouseName());
		address.setStreetName(managerDTO.getAddressDTO().getStreetName());
		address.setCityName(managerDTO.getAddressDTO().getCityName());
		manager.setAddress(address);
		manager.setBranchId(managerDTO.getBranchId());
		System.out.println(KafkaConstant.MESSAGE_LISTENER + manager);
	}
}
