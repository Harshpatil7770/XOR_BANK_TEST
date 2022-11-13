package com.xoriant.bank.resource;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.xoriant.bank.service.ManagerService;
import com.xoriant.bank.service.RuntimeManager;

@RestController
@RequestMapping("/api/manager")
public class ManagerResource {

	private final Logger logger = LoggerFactory.getLogger(ManagerResource.class);

	@Autowired
	ManagerService managerService;

	@Autowired
	private RuntimeManager runtimeManager;

	@Value("${XOR_RETRY_COUNT}")
	private int XOR_RETRY_COUNT;

	// we are not direct connect to mq or db so no need to add thread wait time.
	@PostMapping("/save")
	public ResponseEntity<Manager> addNewManager(@Valid @RequestBody ManagerDTO managerDTO) {
		Manager response = managerService.addNewManager(managerDTO);
		if (response == null) {
			logger.info("Unable to add new Manager Details ");
			return new ResponseEntity<Manager>(response, HttpStatus.BAD_REQUEST);
		} else
			logger.info("New Manager Details Added Succesfully.");
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Manager>> fetchAllManagerDetails() {
		List<Manager> response = null;
		response = managerService.fetchAllManagerDetails();
		if (response == null) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Manager> updateManagerDetails(@Valid @RequestBody ManagerDTO managerDTO,
			@RequestParam int retryCount) {
		Manager result = managerService.updateManagerDetails(managerDTO);
		if (result != null)
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		else
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/find/{managerId}/{retryCount}")
	public ResponseEntity<Manager> findByManagerId(@PathVariable long managerId, @PathVariable int retryCount) {
		Manager response = null;
			response = managerService.findManagerById(managerId);
			if (response == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/find/")
	public ResponseEntity<Manager> findByFirstNameAndLastName(@RequestParam String firstName,
			@RequestParam String lastName) {
		Manager response = managerService.findByFirstNameAndLastName(firstName, lastName);
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/findAll/alphabeticalOrder")
	public ResponseEntity<List<Manager>> fetchAllManagerListsInAlphabeticalOrder() {
		List<Manager> response = managerService.fetchAllManagerListsInAlphabeticalOrder();
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-manager/{branchId}")
	public ResponseEntity<Manager> findManagerByBranchId(@PathVariable long branchId) {
		Manager response = managerService.findManagerByBranchId(branchId);
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{managerId}")
	public ResponseEntity<String> deleteManager(@PathVariable long managerId) {
		boolean response = managerService.deleteManager(managerId);
		if (response == false)
			return new ResponseEntity<>("Unable to delete manager record", HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>("Deleted Manager record succesfully", HttpStatus.OK);
	}
}
