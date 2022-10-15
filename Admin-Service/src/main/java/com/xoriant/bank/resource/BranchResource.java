package com.xoriant.bank.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.ErrorCode;
import com.xoriant.bank.service.BranchService;
import com.xoriant.bank.service.RuntimeManager;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/branch")
@CrossOrigin
@Slf4j
public class BranchResource {

	@Autowired
	private BranchService adminService;

	@Autowired
	private RuntimeManager runtimeManager;

	@PostMapping("/save")
	public ResponseEntity<String> addNewBranch(@Valid @RequestBody BranchDTO branchDTO) {
		try {
			log.info(">>>> addNewBranch() called " + branchDTO);
			boolean response = adminService.addNewBranch(branchDTO);
			if (response == true) {
				return new ResponseEntity<>("New Branch Details Added Succesfully ", HttpStatus.CREATED);
			}
		} catch (Exception e) {
			log.info("BranchResource - exception occured while adding new branch " + e);
			runtimeManager.getErrorController(ErrorCode.NEW_BRANCH_ADDITION_FAILED);
		}
		return new ResponseEntity<>("Unable to add new branch details ", HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateBranchDetails(@Valid @RequestBody BranchDTO branchDTO) {
		try {
			log.info("UpdateBranch() called " + branchDTO);
			boolean response = adminService.updateBranchDetails(branchDTO);
			if (response == true) {
				return new ResponseEntity<>("Updated Exsiting Branch Details Succesfully ", HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("BranchResource - exception occured while updating existing branch details " + e);
			runtimeManager.getErrorController(ErrorCode.NEW_BRANCH_ADDITION_FAILED);
		}
		return new ResponseEntity<>("Unable to update the existing branch details ", HttpStatus.OK);
	}

	@GetMapping("/findAll/branches")
	public ResponseEntity<List<Branch>> findAllBranchesWithAddressDetails() {
		try {
			log.info("findAllBranchesWithAddressDetails() called ");
			List<Branch> response = adminService.findAllBranchesWithAddressDetails();
			if (response != null)
				return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.info("Exception occured while fetching all branch details", e);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/find-branch/{branchId}")
	public ResponseEntity<Branch> findByBranchId(@PathVariable long branchId) {
		log.info("findByBranchId() called ");
		Branch response = adminService.findByBranchId(branchId);
		if (response == null)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-branch")
	public ResponseEntity<Branch> findBranchByName(@RequestParam String branchName) {
		log.info("findBranchByName() called ");
		Branch response = adminService.findBranchByName(branchName);
		if (response != null)
			return new ResponseEntity<>(response, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/delete-branch")
	public ResponseEntity<String> deleteBranch(@RequestParam long branchId) {
		try {
			log.info("deleteBranch() called");
			boolean response = adminService.deleteBranch(branchId);
			if (response == true) {
				return new ResponseEntity<>("Entered Branch Id = " + branchId + " removed succesfully from system",
						HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("BranchResource - exception occured while deleting existing branch details " + e);
			runtimeManager.getErrorController(ErrorCode.NEW_BRANCH_ADDITION_FAILED);
		}
		return new ResponseEntity<>("Unable to delete Existing Branch details", HttpStatus.OK);
	}

	
//	public ResponseEntity<Manager> addNewManager(ManagerDTO managerDTO)

//	Manager updateManagerDetails(ManagerDTO managerDTO);
//
//	List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists);
//
//	List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists);
//
//	List<Manager> fetchAllManagerDetails();
//
//	Manager findByManagerId(long managerId);
//
//	Manager findByFirstNameAndLastName(String firstName, String lastName);
//
//	List<Manager> fetchAllManagerListsInAlphabeticalOrder();
//
//	Manager findManagerByBranchId(long branchId);
//
//	void deleteManager(long managerId);
}
