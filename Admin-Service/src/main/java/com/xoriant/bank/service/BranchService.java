package com.xoriant.bank.service;

import java.util.List;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Manager;

public interface BranchService {

	boolean addNewBranch(BranchDTO branchDTO);
	
	List<Branch> findAllBranchesWithAddressDetails(int retryCount);

	boolean updateBranchDetails(BranchDTO branchDTO);

//	List<Branch> fetchAllBranch(int retryCount);

	Branch findByBranchId(long branchId,int retryCount);

	Branch findBranchByName(String branchName,int retryCount);

	boolean deleteBranch(long branchId,int retryCount);

//	Manager addNewManager(ManagerDTO managerDTO);
//
//	boolean updateManagerDetails(ManagerDTO managerDTO);
//
//	List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists);
//
//	List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists);
//
//	List<Manager> fetchAllManagerDetails();
//
//	boolean findManagerById(long managerId);
//
//	Manager findByFirstNameAndLastName(String firstName, String lastName);
//
//	List<Manager> fetchAllManagerListsInAlphabeticalOrder();
//
//	Manager findManagerByBranchId(long branchId);
//
//	void deleteManager(long managerId);
}
