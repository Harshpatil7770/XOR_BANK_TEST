package com.xoriant.bank.service;

import java.util.List;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.model.Manager;

public interface ManagerService {

	Manager addNewManager(ManagerDTO managerDTO) ;

	Manager updateManagerDetails(ManagerDTO managerDTO);

	List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists);

	List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists);

	List<Manager> fetchAllManagerDetails();

	Manager findManagerById(long managerId);

	Manager findByFirstNameAndLastName(String firstName, String lastName);

	List<Manager> fetchAllManagerListsInAlphabeticalOrder();

	Manager findManagerByBranchId(long branchId);

	boolean deleteManager(long managerId);
}
