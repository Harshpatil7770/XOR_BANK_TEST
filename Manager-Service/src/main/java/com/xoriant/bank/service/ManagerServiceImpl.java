package com.xoriant.bank.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.bank.constant.ManagerServiceConstant;
import com.xoriant.bank.dao.AccountBalanceDetailsRepo;
import com.xoriant.bank.dao.AccountDetailsRepo;
import com.xoriant.bank.dao.AccountTypeRepo;
import com.xoriant.bank.dao.AddressRepo;
import com.xoriant.bank.dao.CustomerRepo;
import com.xoriant.bank.dto.AccountBalanceDetailsDTO;
import com.xoriant.bank.dto.AccountTypeDTO;
import com.xoriant.bank.dto.AddressDTO;
import com.xoriant.bank.dto.CustomerDTO;
import com.xoriant.bank.exception.AccountTypeExpection;
import com.xoriant.bank.exception.CustomerAddressException;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.model.AccountBalanceDetails;
import com.xoriant.bank.model.AccountDetails;
import com.xoriant.bank.model.AccountType;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Customer;
import com.xoriant.bank.model.LoginDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private AccountTypeRepo accountTypeRepo;

	@Autowired
	private AccountDetailsRepo accountDetailsRepo;

	private AccountType accountType;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private AccountBalanceDetailsRepo accountBalanceDetailsRepo;


	@Override
	public Customer addNewCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException {
		Customer customer = new Customer();
		Address address = new Address();
		AccountDetails customerAccountDetails = new AccountDetails();
		AccountBalanceDetails accountBalanceDetails = new AccountBalanceDetails();
		LoginDetails loginDetails = new LoginDetails();
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
		long customerAccountNum = ManagerServiceConstant.getAccountNumber();
		customerAccountDetails.setAccountNumber(customerAccountNum);
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
		accountBalanceDetails.setAccountNumber(customerAccountNum);
		customerAccountDetails.setAccountBalanceDetails(accountBalanceDetails);

		customer.setAccountDetails(customerAccountDetails);
		log.info("Account number generated and Amount added in customer account processing");
		loginDetails.setUserName(customerDTO.getLoginDetailsDTO().getUserName());
		loginDetails.setPassword(customerDTO.getLoginDetailsDTO().getPassword());
		loginDetails.setAccountNumber(customerAccountNum);
		customer.setLoginDetails(loginDetails);
		log.info("Customer username and password added");
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
				log.info("Entered Account Type is present is database");
				return true;
			}
		}
		log.info("Entered Account Type is not present is database");
		return false;
	}

	@Override
	public Customer updateCustomerDetails(CustomerDTO customerDTO, String accountType) throws ParseException {
		Optional<Customer> existingCustomer = customerRepo.findById(customerDTO.getId());
		if (existingCustomer.isPresent()) {
			log.info("Customer want to update basic personal details");
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

			if (customerDTO.getAddressDTO().getCustomerResponse().toUpperCase()
					.equals(ManagerServiceConstant.CUSTOMER_WANT_TO_UPDATE_ADDRESS)) {
				log.info("Customer want to update their address details");
				Optional<Address> existingCustomerAddress = addressRepo
						.findById(customerDTO.getAddressDTO().getAddressId());
				if (existingCustomerAddress == null) {
					log.info("Customer address id not mathced to existing address id");
					Customer customerAddressDetails = customerRepo.findById(customerDTO.getId()).orElse(null);
					Address updateCustomerAddress = new Address();
					updateCustomerAddress.setAddressId(customerAddressDetails.getAddress().getAddressId());
					updateCustomerAddress
							.setAddressLine1(customerAddressDetails.getAddress().getAddressLine1().toUpperCase());
					updateCustomerAddress
							.setAddressLine2(customerAddressDetails.getAddress().getAddressLine2().toUpperCase());
					updateCustomerAddress
							.setAddressLine3(customerAddressDetails.getAddress().getAddressLine3().toUpperCase());
					updateCustomerDetails.setAddress(updateCustomerAddress);
					throw new CustomerAddressException();
				} else {
					Address updateCustomerAddress = addressRepo.findById(customerDTO.getId()).orElse(null);
					updateCustomerAddress.setAddressId(customerDTO.getAddressDTO().getAddressId());
					updateCustomerAddress.setAddressLine1(customerDTO.getAddressDTO().getAddressLine1().toUpperCase());
					updateCustomerAddress.setAddressLine2(customerDTO.getAddressDTO().getAddressLine2().toUpperCase());
					updateCustomerAddress.setAddressLine3(customerDTO.getAddressDTO().getAddressLine3().toUpperCase());
					updateCustomerDetails.setAddress(updateCustomerAddress);
					log.info("Processing customer update details...!");
				}
			} else if (customerDTO.getAddressDTO().getCustomerResponse().toUpperCase()
					.equals(ManagerServiceConstant.CUSTOMER_DONT_WANT_TO_UPDATE_ADDRESS)) {
				log.info("Customer don't want to update their address details");
				Customer customerDetails = customerRepo.findById(customerDTO.getId()).orElse(null);
				Address updateCustomerAddress = new Address();
				updateCustomerAddress.setAddressId(customerDetails.getAddress().getAddressId());
				updateCustomerAddress.setAddressLine1(customerDetails.getAddress().getAddressLine1().toUpperCase());
				updateCustomerAddress.setAddressLine2(customerDetails.getAddress().getAddressLine2().toUpperCase());
				updateCustomerAddress.setAddressLine3(customerDetails.getAddress().getAddressLine3().toUpperCase());
				updateCustomerDetails.setAddress(updateCustomerAddress);
			}
			AccountDetails accountDetails;
			if (customerDTO.getAccountDetailsDTO().getResponse().toUpperCase()
					.equals(ManagerServiceConstant.CUSTOMER_DONT_WANT_TO_UPDATE_ACCOUNT_DETAILS)) {
				log.info("Customer don't want to update their account details");
				accountDetails = new AccountDetails();
				Optional<AccountDetails> existingAccountNumber = accountDetailsRepo
						.findById(customerDTO.getAccountDetailsDTO().getAccountNumber());
				log.info("Customer don't want to update their account details");
				Customer customerDetails = customerRepo.findById(customerDTO.getId()).orElse(null);
				accountDetails.setAccountNumber(customerDetails.getAccountDetails().getAccountNumber());
				accountDetails.setAccountType(customerDetails.getAccountDetails().getAccountType());
				accountDetails.setAccountOpeningDate(customerDetails.getAccountDetails().getAccountOpeningDate());

				AccountBalanceDetails accountBalanceDetails = new AccountBalanceDetails();
				accountBalanceDetails.setTransactinId(
						customerDetails.getAccountDetails().getAccountBalanceDetails().getTransactinId());
				accountBalanceDetails.setCreaditAmount(
						customerDetails.getAccountDetails().getAccountBalanceDetails().getCreaditAmount());
				accountBalanceDetails.setDebitAmount(
						customerDetails.getAccountDetails().getAccountBalanceDetails().getDebitAmount());
				accountBalanceDetails.setAccountBalance(
						customerDetails.getAccountDetails().getAccountBalanceDetails().getAccountBalance());
				accountBalanceDetails.setAccountNumber(
						customerDetails.getAccountDetails().getAccountBalanceDetails().getAccountNumber());
				accountDetails.setAccountBalanceDetails(accountBalanceDetails);
				updateCustomerDetails.setAccountDetails(accountDetails);
			} else {
				accountDetails = new AccountDetails();
				log.info("Customer want to update their account details");
				Customer customerDetails = customerRepo.findById(customerDTO.getId()).orElse(null);
				accountDetails.setAccountNumber(customerDetails.getAccountDetails().getAccountNumber());
				boolean isExistAccountType = findByName(accountType);
				if (isExistAccountType == true) {
					int accountTypeInfo = accountTypeRepo.findAccountTypeId(accountType);
					accountDetails.setAccountType(accountTypeInfo);
				}
				accountDetails.setAccountOpeningDate(customerDetails.getAccountDetails().getAccountOpeningDate());
				AccountBalanceDetails accountBalanceDetails = new AccountBalanceDetails();
				if (customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getResponse().toUpperCase()
						.equals(ManagerServiceConstant.CUSTOMER_WANT_TO_UPDATE_ACCOUNT_BALANCE_DETAILS)) {
					log.info("Customer want to update their account balance details");
					accountBalanceDetails.setCreaditAmount(
							customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getCreaditAmount());
					accountBalanceDetails.setDebitAmount(
							customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getDebitAmount());
					accountBalanceDetails.setAccountBalance(
							customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getAccountBalance());
					accountBalanceDetails.setAccountNumber(
							customerDTO.getAccountDetailsDTO().getAccountBalanceDetailsDTO().getAccountNumber());
					accountDetails.setAccountBalanceDetails(accountBalanceDetails);
					updateCustomerDetails.setAccountDetails(accountDetails);
				} else {
					log.info("Customer don't want to update their account balance details");
					accountBalanceDetails.setCreaditAmount(
							customerDetails.getAccountDetails().getAccountBalanceDetails().getCreaditAmount());
					accountBalanceDetails.setDebitAmount(
							customerDetails.getAccountDetails().getAccountBalanceDetails().getDebitAmount());
					accountBalanceDetails.setAccountBalance(
							customerDetails.getAccountDetails().getAccountBalanceDetails().getAccountBalance());
					accountBalanceDetails.setAccountNumber(
							customerDetails.getAccountDetails().getAccountBalanceDetails().getAccountNumber());
					accountDetails.setAccountBalanceDetails(accountBalanceDetails);
					updateCustomerDetails.setAccountDetails(accountDetails);
				}
			}
			log.info("updated existing customer account details.........!");

			return customerRepo.save(updateCustomerDetails);

		} else {
			throw new ElementNotFoundException();
		}
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
		return newCustomerAccountLists;
	}

	@Override
	public String deleteCustomerAccount(long id) {
		Optional<Customer> existingCustomerAccount = customerRepo.findById(id);
		if (!existingCustomerAccount.isPresent()) {
			log.info("Entered customered id is not present in database");
			throw new ElementNotFoundException();
		}
		customerRepo.deleteById(id);
		log.info("Entered Account id is present in database");
		log.info("Deleted customer account succesfully ");
		return ManagerServiceConstant.DELETED_CUSTOMER_ACCOUNT;

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
		Optional<Customer> isExistCustomerDetails = customerRepo.findByFirstNameAndLastName(firstName, lastName);
		if (!isExistCustomerDetails.isPresent()) {
			log.info("Entered first and last name not present in database");
			throw new ElementNotFoundException();
		}
		log.info("Entered first and last name present in database");
		return isExistCustomerDetails;
	}

	@Override
	public List<Customer> findAllCustomerDetails() {
		List<Customer> existingCustomerDetails = customerRepo.findByInfoWall();
		if (existingCustomerDetails == null || existingCustomerDetails.isEmpty()) {
			log.info("Customer records not present in database");
			throw new ElementNotFoundException();
		}
		log.info("Customer records present in database");
		return existingCustomerDetails;
	}

	@Override
	public Customer findCustomerAccountById(long id) {
		Customer isExistingAccount = customerRepo.findById(id).orElse(null);
		if (isExistingAccount == null) {
			log.info("entered customer account id is not present in database");
			throw new ElementNotFoundException();
		}
		log.info("Entered customer account id is present is database");
		return isExistingAccount;
	}

	@Override
	public List<Customer> findAllCustomerDetailsWithIncreasingOrderOfAccountNumber() {
		List<Customer> fetchAllAccountDetails = customerRepo.findAll();
//		List<Customer> increasingOrderAccountNumberLists = null;
		// List<AccountDetails>
		// fetchCustomerDetails=fetchAllAccountDetails.stream().sorted(Comparator.comparingLong(AccountDetails::getAccountNumber)).c
//		List<Customer> increasingOrderAccountNumberLists = fetchAllAccountDetails.stream()
//				.sorted(Comparator.comparingLong(Customer::getAccountDetails)).collect(Collectors.toList());

		return null;
	}

	@Override
	public List<Customer> findAllCustomerDetailsWithAlphabeticalorder() {
		List<Customer> fetchAllCustomerDetails = customerRepo.findAll();
		if (fetchAllCustomerDetails == null) {
			log.info("Customer records not present in database");
		}
		log.info("Customer records present in database");
		List<Customer> fetchByFirstCharacterSequntialManner = fetchAllCustomerDetails.stream()
				.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

		log.info("Arranging all customer records in alphabetical order...");
		return fetchByFirstCharacterSequntialManner;
	}

	@Override
	public Customer findByAccountNumber(long accountDetails) {
		AccountDetails isExistCustomerAccount = accountDetailsRepo.findById(accountDetails).orElse(null);
		if (isExistCustomerAccount == null) {
			log.info("Entered Account number not present in database");
			throw new ElementNotFoundException();
		}
		log.info("Entered Accoont number present in database");
		Customer fetchExistingCustomer = customerRepo.findByAccountDetails(isExistCustomerAccount);
		return fetchExistingCustomer;
	}

	@Override
	public double checkCustomerAccountBalance(long accountDetails) {
		Customer isExistingAccount = findByAccountNumber(accountDetails);
		double remainingCustomerAccountBalance = isExistingAccount.getAccountDetails().getAccountBalanceDetails()
				.getAccountBalance();

		log.info("Customer account present in database");
		return remainingCustomerAccountBalance;
	}

}
