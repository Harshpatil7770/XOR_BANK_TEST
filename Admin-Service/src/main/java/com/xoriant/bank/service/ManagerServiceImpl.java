package com.xoriant.bank.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.exception.InputUserException;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.model.ManagerCredential;
import com.xoriant.bank.repo.BranchRepo;
import com.xoriant.bank.repo.ManagerRepo;
import com.xoriant.bank.util.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerServiceImplDetails managerServiceImplDetails;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Override
	public Manager addNewManager(ManagerDTO managerDTO) {
		if (managerDTO != null) {
			Manager managerDetails = managerServiceImplDetails.addNewManager(managerDTO);
			return managerDetails;
		}
		log.info("ManagerDTO is null. No need to proceed further.");
		return null;
	}

	@Override
	public Manager updateManagerDetails(ManagerDTO managerDTO) {
		Manager updateManager = null;
		if (managerDTO != null) {
			updateManager = managerServiceImplDetails.updateManagerDetails(managerDTO);
			return updateManager;
		} else
			return null;
	}

	private boolean findManagerCredentialDetails(int id, String userName, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Manager> fetchAllManagerDetails() {
		List<Manager> existingManagerLists = managerRepo.findAllManagerWithTheirBranchDetails();
		if (existingManagerLists.isEmpty()) {
			log.info("Manager details not found in system. No need to proceed further.");
			return null;
		}
		log.info("Manager details found in system");
		return existingManagerLists;
	}

	@Override
	public Manager findManagerById(long managerId) {
		Manager existingManager = managerRepo.findById(managerId).orElse(null);
		if (existingManager == null)
			return null;
		else
			return existingManager;
	}

	@Override
	public Manager findByFirstNameAndLastName(String firstName, String lastName) {
		Manager existingManager = managerRepo.findByFirstNameAndLastName(firstName, lastName);
		if (existingManager == null) {
			log.info("Entered manager details not found system.");
			return null;
		} else
			log.info("Entered manager details found system.");
		return existingManager;
	}

	@Override
	public List<Manager> fetchAllManagerListsInAlphabeticalOrder() {
		List<Manager> existingManagerLists = managerRepo.findAll();
		if (existingManagerLists == null) {
			log.info("Manager details not present in system.");
			return null;
		}
		List<Manager> alphabeticalOrderManLists = existingManagerLists.stream()
				.sorted((e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName())).collect(Collectors.toList());
		return alphabeticalOrderManLists;
	}

	@Override
	public Manager findManagerByBranchId(long branchId) {
		Branch existingBranch = branchRepo.findById(branchId).orElse(null);
		if (existingBranch == null) {
			log.info("Entered branch id details not found in system.");
			return null;
		}
		Manager existingManagerDetails = managerRepo.findByBranchId(branchId).orElse(null);
		if (existingManagerDetails == null) {
			log.info("Entered branch id not assigned to any manager.");
			return null;
		}
		return existingManagerDetails;
	}

	@Override
	public boolean deleteManager(long managerId) {
		Manager existingManger = managerRepo.findById(managerId).orElse(null);
		if (existingManger == null) {
			log.info("Entered manager id details not present in system.");
			return false;
		}
		managerRepo.deleteById(managerId);
		log.info("Deleted manager details succesfully");
		return true;
	}

}
