package com.xoriant.bank.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.service.BranchService;
import com.xoriant.bank.service.ManagerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/manager")
@Slf4j
public class ManagerResource {

	@Autowired
	ManagerService managerService;

	@PostMapping("/save")
	public ResponseEntity<Manager> addNewManager(@Valid @RequestBody ManagerDTO managerDTO) {
		log.info("addNewManager() called");
		Manager response = managerService.addNewManager(managerDTO);
		if (response == null) {
			log.info("Unable to add new Manager Details ");
			return new ResponseEntity<Manager>(response, HttpStatus.BAD_REQUEST);
		}
		log.info("New Manager Added Succesfully.");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Manager>> fetchAllManagerDetails() {
		log.info("fetchAllManagerDetails() called");
		List<Manager> response = managerService.fetchAllManagerDetails();
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Manager> updateManagerDetails(@Valid @RequestBody ManagerDTO managerDTO) {
		log.info("updateManagerDetails() called");
		Manager result = managerService.updateManagerDetails(managerDTO);
		return new ResponseEntity<Manager>(result, HttpStatus.CREATED);
	}

	@GetMapping("/find/{managerId}")
	public ResponseEntity<Manager> findByManagerId(@PathVariable long managerId) {
		log.info("findByManagerId() called");
		Manager response = managerService.findManagerById(managerId);
		if (response == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find/")
	public ResponseEntity<Manager> findByFirstNameAndLastName(@RequestParam String firstName,
			@RequestParam String lastName) {
		log.info("findByFirstNameAndLastName() called");
		Manager response = managerService.findByFirstNameAndLastName(firstName, lastName);
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/findAll/alphabeticalOrder")
	public ResponseEntity<List<Manager>> fetchAllManagerListsInAlphabeticalOrder() {
		log.info("fetchAllManagerListsInAlphabeticalOrder() called");
		List<Manager> response = managerService.fetchAllManagerListsInAlphabeticalOrder();
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/find-manager/{branchId}")
	public ResponseEntity<Manager> findManagerByBranchId(@PathVariable long branchId) {
		log.info("findManagerByBranchId() called");
		Manager response = managerService.findManagerByBranchId(branchId);
		if(response==null)
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{managerId}")
	public ResponseEntity<String> deleteManager(@PathVariable long managerId) {
		log.info("deleteManager() called");
		boolean response = managerService.deleteManager(managerId);
		if(response==false)
			return new ResponseEntity<>("Unable to delete manager record",HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>("Deleted Manager record succesfully",HttpStatus.OK);
	}
}
