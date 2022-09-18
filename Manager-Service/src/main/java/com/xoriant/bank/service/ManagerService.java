package com.xoriant.bank.service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.xoriant.bank.dto.AccountTypeDTO;
import com.xoriant.bank.dto.CustomerDTO;
import com.xoriant.bank.dto.LoginDetailsDTO;
import com.xoriant.bank.model.AccountBalanceDetails;
import com.xoriant.bank.model.AccountDetails;
import com.xoriant.bank.model.AccountType;
import com.xoriant.bank.model.Customer;
import com.xoriant.bank.model.LoginDetails;

public interface ManagerService {

	Customer addNewCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException;

	AccountType addNewAccountType(AccountTypeDTO accountTypeDTO);

	boolean findByName(String accountType);

	Customer updateCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException;

	List<Customer> addNewListsOfSavingCustomerAccount(List<CustomerDTO> customerDTOLists, String accountType)
			throws ParseException;

	String deleteCustomerAccount(long id);

	Customer findCustomerAccountById(long id);

	Optional<Customer> findCustomerAccountByFirstAndLastName(String firstName, String lastName);

	List<Customer> findAllCustomerDetails();

	List<Customer> findAllCustomerDetailsWithIncreasingOrderOfAccountNumber();

	List<Customer> findAllCustomerDetailsWithAlphabeticalorder();

	Customer findByAccountNumber(long accountDetails);

	double checkCustomerAccountBalance(long accountDetails);

	LoginDetails updateCustomerPassword(long accountNumber, String oldPassword, LoginDetailsDTO loginDetailsDTO);

	/*
	 * 1) when customer send request to add money then add //feign client
	 * 
	 */

}
