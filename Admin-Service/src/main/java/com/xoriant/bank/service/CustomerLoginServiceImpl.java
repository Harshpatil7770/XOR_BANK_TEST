package com.xoriant.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.bank.constant.LoginConstantDetails;
import com.xoriant.bank.dto.LoginConstantDTO;
import com.xoriant.bank.model.LoginConstant;
import com.xoriant.bank.repo.CustomerLoginRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerLoginServiceImpl implements CustomerLoginService {

	@Autowired
	private CustomerLoginRepo customerLoginRepo;

	@Override
	public LoginConstant addNewCustomerAccount(LoginConstantDTO loginConstantDTO) {
		LoginConstant loginConstant = new LoginConstant();

		loginConstant.setAccountNumber(LoginConstantDetails.getAccountNumber());
		loginConstant.setUserName(loginConstantDTO.getUserName());
		loginConstant.setPassword(loginConstantDTO.getPassword());
		log.info("New Customer Authentication Details Added");
		return customerLoginRepo.save(loginConstant);
	}

}
