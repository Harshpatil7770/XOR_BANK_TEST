package com.xoriant.bank.service;

import java.util.List;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Manager;

public interface BranchService {

	Branch addNewBranch(BranchDTO branchDTO,int retryCount);
	
	List<Branch> findAllBranchesWithAddressDetails();

	Branch updateBranchDetails(BranchDTO branchDTO);

	List<Branch> fetchAllBranch();

	Branch findByBranchId(long branchId);

	Branch findBranchByName(String branchName);

	boolean deleteBranch(long branchId);

	Manager addNewManager(ManagerDTO managerDTO);

	boolean updateManagerDetails(ManagerDTO managerDTO);

	List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists);

	List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists);

	List<Manager> fetchAllManagerDetails();

	boolean findManagerById(long managerId);

	Manager findByFirstNameAndLastName(String firstName, String lastName);

	List<Manager> fetchAllManagerListsInAlphabeticalOrder();

	Manager findManagerByBranchId(long branchId);

	void deleteManager(long managerId);
}
