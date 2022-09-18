package com.xoriant.bank.service;

import com.xoriant.bank.dto.LoginConstantDTO;
import com.xoriant.bank.model.LoginConstant;

public interface CustomerLoginService {

	public LoginConstant addNewCustomerAccount(LoginConstantDTO loginConstantDTO);
}
