package com.xoriant.bank.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.bank.constant.ManagerServiceConstant;
import com.xoriant.bank.dao.AccountTypeRepo;
import com.xoriant.bank.dao.AddressRepo;
import com.xoriant.bank.dao.CustomerRepo;
import com.xoriant.bank.dto.AccountTypeDTO;
import com.xoriant.bank.dto.CustomerDTO;
import com.xoriant.bank.exception.AccountTypeExpection;
import com.xoriant.bank.exception.CustomerAddressException;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.model.AccountBalanceDetails;
import com.xoriant.bank.model.AccountDetails;
import com.xoriant.bank.model.AccountType;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private AccountTypeRepo accountTypeRepo;

	private AccountType accountType;

	@Autowired
	private AddressRepo addressRepo;

	@Override
	public Customer addNewCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException {
		Customer customer = new Customer();
		Address address = new Address();
		AccountDetails customerAccountDetails = new AccountDetails();
//		AccountType accountType = new AccountType();
		AccountBalanceDetails accountBalanceDetails = new AccountBalanceDetails();
		customer.setId(customerDTO.getId());
		customer.setFirstName(customerDTO.getFirstName().toUpperCase());
		customer.setLastName(customerDTO.getLastName().toUpperCase());
		customer.setGender(customerDTO.getGender().toUpperCase());
		customer.setAdharNumber(customerDTO.getAdharNumber());
		customer.setMobileNumber(customerDTO.getMobileNumber());
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
		Date customerDOB = simpleDateFormat1.parse(customerDTO.getDateOfBirth());
		log.info("Customer DOB " + customerDOB);
		customer.setDateOfBirth(customerDOB);
		log.info("Customer Basic Details Added ..........!");

		address.setAddressLine1(customerDTO.getAddressDTO().getAddressLine1().toUpperCase());
		address.setAddressLine2(customerDTO.getAddressDTO().getAddressLine2().toUpperCase());
		address.setAddressLine3(customerDTO.getAddressDTO().getAddressLine3().toUpperCase());
		customer.setAddress(address);
		log.info("Customer Home address details added .........!");

		customerAccountDetails.setAccountNumber(ManagerServiceConstant.getAccountNumber());
		boolean accountTypeResult = findByName(accountType);
		if (accountTypeResult == true) {
			int accountTypeResponse = accountTypeRepo.findAccountTypeId(accountType);
			customerAccountDetails.setAccountType(accountTypeResponse);

		} else {
			throw new AccountTypeExpection();
		}
		log.info("Account Type Details Added ........!");

		customerAccountDetails.setAccountOpeningDate(ManagerServiceConstant.getCurrentDate());
		accountBalanceDetails
				.setCreaditAmount(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getCreaditAmount());
		accountBalanceDetails
				.setDebitAmount(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getDebitAmount());
		accountBalanceDetails
				.setAccountBalance(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getCreaditAmount());
		customerAccountDetails.setAccountBalanceDetails(accountBalanceDetails);
		customer.setAccountDetails(customerAccountDetails);

		log.info("Succesfully entered customer details ........!!!");
		log.info("Customer new account opened succesfully ..........!!!");
		customerRepo.save(customer);

		return customer;
	}

	@Override
	public AccountType addNewAccountType(AccountTypeDTO accountTypeDTO) {

		accountType = new AccountType();
		accountType.setAccountType(accountTypeDTO.getAccountType().toUpperCase());
		log.info("New Account Type Added .......!");
		return accountTypeRepo.save(accountType);
	}

	@Override
	public boolean findByName(String accountType) {
		int count = 0;
		String customerAccountType = accountType.toUpperCase();
		List<String> fetchAllAccountTypes = accountTypeRepo.findByAccountType();
		if (fetchAllAccountTypes != null) {
			for (String existingAccountType : fetchAllAccountTypes) {
				if (existingAccountType.equals(customerAccountType)) {
					count++;
				}
			}
			if (count == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Customer updateCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException {
		Optional<Customer> existingCustomer = customerRepo.findById(customerDTO.getId());
		if (!existingCustomer.isPresent()) {
			throw new ElementNotFoundException();
		}

		Customer updateCustomerDetails = customerRepo.findById(customerDTO.getId()).orElse(null);
		updateCustomerDetails.setId(customerDTO.getId());
		updateCustomerDetails.setFirstName(customerDTO.getFirstName().toUpperCase());
		updateCustomerDetails.setLastName(customerDTO.getLastName().toUpperCase());
		updateCustomerDetails.setGender(customerDTO.getGender().toUpperCase());
		updateCustomerDetails.setAdharNumber(customerDTO.getAdharNumber());
		updateCustomerDetails.setMobileNumber(customerDTO.getMobileNumber());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = simpleDateFormat.parse(customerDTO.getDateOfBirth());
		updateCustomerDetails.setDateOfBirth(date);

		long customerId = customerDTO.getId();
		Optional<Address> existingCustomerAddress = addressRepo.findById(customerDTO.getAddressDTO().getAddressId());

		if (existingCustomerAddress == null) {
			log.info("Customer Don't want to update address.........!");
			throw new CustomerAddressException();
		} else {
			Address updateCustomerAddress = addressRepo.findById(customerDTO.getId()).orElse(null);
//			Customer existingCustomerDetails = findCustomerAccountById(customerId);
//			if (existingCustomerDetails.getAddress().getAddressId() == customerDTO.getAddressDTO().getAddressId()) {
//				throw new CustomerAddressException();
//			}
//			updateCustomerAddress.setAddressId(customerDTO.getAddressDTO().getAddressId());
			updateCustomerAddress.setAddressLine1(customerDTO.getAddressDTO().getAddressLine1().toUpperCase());
			updateCustomerAddress.setAddressLine2(customerDTO.getAddressDTO().getAddressLine2().toUpperCase());
			updateCustomerAddress.setAddressLine3(customerDTO.getAddressDTO().getAddressLine3().toUpperCase());
			updateCustomerDetails.setAddress(updateCustomerAddress);
		}
		AccountDetails accountDetails = new AccountDetails();
		boolean accountResult = findByName(accountType.toUpperCase());
		if (accountResult == false) {
			throw new AccountTypeExpection();
		}
		int accountTypeId = accountTypeRepo.findAccountTypeId(accountType);
		accountDetails.setAccountType(accountTypeId);

		updateCustomerDetails.setAccountDetails(accountDetails);
		log.info("updated existing customer account details.........!");

		return customerRepo.save(updateCustomerDetails);
	}

//	public boolean checkCustomerAddressPresentOrNot(long addressId) {
//		Optional<Address> existingCustomerAddress = findCustomerAccountById();
//		if (existingCustomerAddress == null) {
//			return false;
//		}
//		return true;
//	}

	@Override
	public List<Customer> addNewListsOfSavingCustomerAccount(List<CustomerDTO> customerDTOLists, String accountType)
			throws ParseException {
		List<Customer> newCustomerAccountLists = new ArrayList<>();
		for (CustomerDTO customerDTO : customerDTOLists) {
			Customer customerAccountResponse = addNewCustomerDetails(customerDTO, accountType);
			newCustomerAccountLists.add(customerAccountResponse);
			log.info("Customer Saving account created with id :: " + customerAccountResponse.getId());
		}
		
//		Customer customer = new Customer();
//		Address address = new Address();
//		AccountDetails customerAccountDetails = new AccountDetails();
////		AccountType accountType = new AccountType();
//		AccountBalanceDetails accountBalanceDetails = new AccountBalanceDetails();
//		customer.setId(customerDTO.getId());
//		customer.setFirstName(customerDTO.getFirstName().toUpperCase());
//		customer.setLastName(customerDTO.getLastName().toUpperCase());
//		customer.setGender(customerDTO.getGender().toUpperCase());
//		customer.setAdharNumber(customerDTO.getAdharNumber());
//		customer.setMobileNumber(customerDTO.getMobileNumber());
//		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
//		Date customerDOB = simpleDateFormat1.parse(customerDTO.getDateOfBirth());
//		log.info("Customer DOB " + customerDOB);
//		customer.setDateOfBirth(customerDOB);
//		log.info("Customer Basic Details Added ..........!");
//
//		address.setAddressLine1(customerDTO.getAddressDTO().getAddressLine1().toUpperCase());
//		address.setAddressLine2(customerDTO.getAddressDTO().getAddressLine2().toUpperCase());
//		address.setAddressLine3(customerDTO.getAddressDTO().getAddressLine3().toUpperCase());
//		customer.setAddress(address);
//		log.info("Customer Home address details added .........!");
//
//		customerAccountDetails.setAccountNumber(ManagerServiceConstant.getAccountNumber());
//		boolean accountTypeResult = findByName(accountType);
//		if (accountTypeResult == true) {
//			int accountTypeResponse = accountTypeRepo.findAccountTypeId(accountType);
//			customerAccountDetails.setAccountType(accountTypeResponse);
//
//		} else {
//			throw new AccountTypeExpection();
//		}
//		log.info("Account Type Details Added ........!");
//
//		customerAccountDetails.setAccountOpeningDate(ManagerServiceConstant.getCurrentDate());
//		accountBalanceDetails
//				.setCreaditAmount(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getCreaditAmount());
//		accountBalanceDetails
//				.setDebitAmount(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getDebitAmount());
//		accountBalanceDetails
//				.setAccountBalance(customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getCreaditAmount());
//		customerAccountDetails.setAccountBalanceDetails(accountBalanceDetails);
//		customer.setAccountDetails(customerAccountDetails);
//
//		log.info("Succesfully entered customer details ........!!!");
//		log.info("Customer new account opened succesfully ..........!!!");
//		customerRepo.save(customer);
//
//		return customer;
		
		return newCustomerAccountLists;
	}

	@Override
	public String updateListsOfCustomerDetails(List<CustomerDTO> customerDTOLists, String accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteCustomerAccount(int customerId) {
		// TODO Assuto-generated method stub
		return null;
	}

//	public Customer findCustomerAccountById(long customerId) {
//		Customer existingCustomer = customerRepo.findCustomerAccountById(customerId);
//		if (existingCustomer == null) {
//			throw new CustomerPresentException();
//		}
//		return existingCustomer;
//	}

	@Override
	public Optional<Customer> findCustomerAccountByFirstAndLastName(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Customer> findAllCustomerDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findCustomerAccountById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
