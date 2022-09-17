package com.xoriant.bank.resource;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.bank.dto.AccountTypeDTO;
import com.xoriant.bank.dto.CustomerDTO;
import com.xoriant.bank.model.AccountType;
import com.xoriant.bank.model.Customer;
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
	public Customer updateCustomerDetails(@RequestBody CustomerDTO customerDTO, @PathVariable String accountType)
			throws ParseException {
		return managerService.updateCustomerDetails(customerDTO, accountType);
	}

	@PostMapping("/saveAll-customer/{accountType}")
	public ResponseEntity<List<Customer>> addNewListsOfSavingCustomerAccount(List<CustomerDTO> customerDTOLists,
			String accountType) throws ParseException {
		List<Customer> response = managerService.addNewListsOfSavingCustomerAccount(customerDTOLists, accountType);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
