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
import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.sender.AdminMsgSender;
import com.xoriant.bank.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/branch")
@CrossOrigin
@Slf4j
public class BranchResource {

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminMsgSender loginMsgSender;

	@PostMapping("/save")
	public ResponseEntity<Branch> addNewBranch(@Valid @RequestBody BranchDTO branchDTO) {
		log.info(">>>> addNewBranch() called " + branchDTO);
		Branch response = adminService.addNewBranch(branchDTO);
		if (response != null) {
			loginMsgSender.addNewBranchDetails(" NEW BRANCH DETAILS ADDED >>> " + " BRANCH_ID :: "
					+ response.getBranchId() + " BRACH_NAME :: " + response.getBranchName());
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/branch")
	public ResponseEntity<Branch> updateBranchDetails(@Valid @RequestBody BranchDTO branchDTO) {
		log.info("UpdateBranch() called " + branchDTO);
		Branch response = adminService.updateBranchDetails(branchDTO);
		if (response != null) {
			loginMsgSender.updateBranchDetails("Update existing Branch details succesfully >>> BRACH_ID :: "
					+ response.getBranchId() + "" + "BRANCH_NAME :: " + response.getBranchName());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/findAll/branches")
	public ResponseEntity<List<Branch>> findAllBranchesWithAddressDetails() {
		log.info("findAllBranchesWithAddressDetails() called ");
		List<Branch> response = adminService.findAllBranchesWithAddressDetails();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-branch/{branchId}")
	public ResponseEntity<Branch> findByBranchId(@PathVariable long branchId) {
		log.info("findByBranchId() called ");
		Branch response = adminService.findByBranchId(branchId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/find-branch")
	public ResponseEntity<Branch> findBranchByName(@RequestParam String branchName) {
		log.info("findBranchByName() called ");
		Branch response = adminService.findBranchByName(branchName);
		return new ResponseEntity<Branch>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete-branch")
	public ResponseEntity<String> deleteBranch(@RequestParam long branchId) {
		log.info("deleteBranch() called");
		boolean response = adminService.deleteBranch(branchId);
		if (!response) {
			log.info("Entered Branch Id = " + branchId + " details not present in system");
			return new ResponseEntity<>("Entered Branch Id = " + branchId + " details not present in system",
					HttpStatus.OK);
		}
		log.info("Entered Branch Id = " + branchId + " details not present in system removed succesfully from system");
		return new ResponseEntity<>("Entered Branch Id = " + branchId + " removed succesfully from system",
				HttpStatus.OK);
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
