package com.xoriant.bank.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.sender.LoginMsgSender;
import com.xoriant.bank.service.BranchService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@Slf4j
public class AdminResource {

//	@Autowired
//	private AdminService adminService;
//	
//	@Autowired
//	private LoginMsgSender loginMsgSender;
//
//	@PostMapping("/add/branch")
//	public ResponseEntity<Branch> addNewBranch(@Valid  @RequestBody BranchDTO branchDTO) {
//		log.info(">>>> addNewBranch() called " + branchDTO);
//		Branch response = adminService.addNewBranch(branchDTO);
//		if(response!=null) {
//			loginMsgSender.addNewBranchDetails(" NEW BRANCH DETAILS ADDED >>> "+" BRANCH_ID :: "+response.getBranchId()+" BRACH_NAME :: "+response.getBranchName());
//			}
//		return new ResponseEntity<>(response, HttpStatus.CREATED);
//	}
//
//	
//	@PutMapping("/update/branch")
//	public ResponseEntity<Branch> updateBranchDetails(@Valid @RequestBody BranchDTO branchDTO) {
//		log.info("UpdateBranch() called "+branchDTO);
//		Branch	response=adminService.updateBranchDetails(branchDTO);
//		if(response!=null) {
//			loginMsgSender.updateBranchDetails("Update existing Branch details succesfully >>> BRACH_ID :: "+response.getBranchId()+""
//					+ "BRANCH_NAME :: "+response.getBranchName());
//		}	
//		return new ResponseEntity<>(response,HttpStatus.OK);
//	}

//	List<Branch> fetchAllBranch();
//
//	Branch findByBranchId(long branchId);
//
//	Branch findByName(String branchName);
//
//	void deleteBranch(long branchId);
//
//	Manager addNewManager(ManagerDTO managerDTO);
//
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
