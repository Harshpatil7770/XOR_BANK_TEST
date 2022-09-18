package com.xoriant.bank.resource;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.bank.dto.AccountTypeDTO;
import com.xoriant.bank.dto.CustomerDTO;
import com.xoriant.bank.dto.LoginDetailsDTO;
import com.xoriant.bank.model.AccountDetails;
import com.xoriant.bank.model.AccountType;
import com.xoriant.bank.model.Customer;
import com.xoriant.bank.model.LoginDetails;
import com.xoriant.bank.service.ManagerService;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin
public class ManagerResource {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplateForNewAccount;

	@PostMapping("/save")
	public ResponseEntity<AccountType> addNewAccountType(@Valid @RequestBody AccountTypeDTO accountTypeDTO) {
		AccountType accountType = managerService.addNewAccountType(accountTypeDTO);
		return new ResponseEntity<>(accountType, HttpStatus.CREATED);
	}

	@GetMapping("/find/{accountType}")
	public boolean findByName(@PathVariable String accountType) {
		return managerService.findByName(accountType);
	}

	@PostMapping("/add/customer/{accountType}")
	public ResponseEntity<Customer> addNewCustomerDetails(@RequestBody CustomerDTO customerDTO,
			@PathVariable String accountType) throws ParseException {
		Customer response = managerService.addNewCustomerDetails(customerDTO, accountType);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/{accountType}")
	public ResponseEntity<Customer> updateCustomerDetails(@RequestBody CustomerDTO customerDTO,
			@PathVariable String accountType) throws ParseException {
		Customer updatedCustomerDetailsResposne = managerService.updateCustomerDetails(customerDTO, accountType);
		return new ResponseEntity<>(updatedCustomerDetailsResposne, HttpStatus.OK);
	}

	@PostMapping("/saveAll-customer/{accountType}")
	public ResponseEntity<List<Customer>> addNewListsOfSavingCustomerAccount(List<CustomerDTO> customerDTOLists,
			String accountType) throws ParseException {
		List<Customer> response = managerService.addNewListsOfSavingCustomerAccount(customerDTOLists, accountType);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCustomerAccount(@RequestParam long id) {
		String response = managerService.deleteCustomerAccount(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/fetchall-infowall")
	public List<Customer> findAllCustomerDetails() {
		return managerService.findAllCustomerDetails();
	}

	@GetMapping("/fetch-customer")
	public ResponseEntity<Optional<Customer>> findCustomerAccountByFirstAndLastName(@RequestParam String firstName,
			@RequestParam String lastName) {
		Optional<Customer> response = managerService.findCustomerAccountByFirstAndLastName(firstName.toUpperCase(),
				lastName.toUpperCase());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-customer/{id}")
	public ResponseEntity<Customer> findCustomerAccountById(@PathVariable long id) {
		Customer response = managerService.findCustomerAccountById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@GetMapping("/find-customer/increasingOrder")
//	public ResponseEntity<List<AccountDetails>> findAllCustomerDetailsWithIncreasingOrderOfAccountNumber() {
//		// List<AccountDetails> response =
//		// managerService.findAllCustomerDetailsWithIncreasingOrderOfAccountNumber();
//		// return new ResponseEntity<>(response, HttpStatus.OK);
//		return null;
//	}

	@GetMapping("/fetchAll-customer/alphabeticalOrder")
	public ResponseEntity<List<Customer>> findAllCustomerDetailsWithAlphabeticalorder() {
		List<Customer> response = managerService.findAllCustomerDetailsWithAlphabeticalorder();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-customer/account/{accountDetails}")
	public ResponseEntity<Customer> findByAccountNumber(@PathVariable long accountDetails) {
		Customer response = managerService.findByAccountNumber(accountDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/check/customer-accountbalance")
	public ResponseEntity<Double> checkCustomerAccountBalance(@RequestParam long accountDetails) {
		double response = managerService.checkCustomerAccountBalance(accountDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update/customer/{accountNumber}/{oldPassword}")
	public ResponseEntity<LoginDetails> updateCustomerPassword(@PathVariable long accountNumber,
			@PathVariable String oldPassword, @RequestBody LoginDetailsDTO loginDetailsDTO) {
		LoginDetails response = managerService.updateCustomerPassword(accountNumber, oldPassword, loginDetailsDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
