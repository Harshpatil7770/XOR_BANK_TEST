package com.xoriant.bank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.exception.InputUserException;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.repo.AddressRepo;
import com.xoriant.bank.repo.BranchRepo;
import com.xoriant.bank.repo.ManagerRepo;
import com.xoriant.bank.util.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private AddressRepo addressRepo;

	private Branch branch;
	private Address branchAddress;

	private Manager manager;
	private Address managerHomeAddress;

	@Override
	public Branch addNewBranch(@Valid BranchDTO branchDTO) {
		// ----------- Branch Details --------------//
		branch = new Branch();
		branch.setBranchId(branchDTO.getBranchId());
		branch.setBranchName(branchDTO.getBranchName().toUpperCase());
		if (branch.getBranchName().isBlank() || branch.getBranchName().isEmpty()) {
			throw new InputUserException();
		}
		branch.setIfscCode(branchDTO.getIfscCode());
		if (branch.getIfscCode().isBlank() || branch.getIfscCode().isEmpty()) {
			throw new InputUserException();
		}
		// ------- Branch Address Details ----------//
		branchAddress = new Address();
		branchAddress.setAddressId(branchDTO.getAddressDTO().getAddressId());
		branchAddress.setHouseNumber(branchDTO.getAddressDTO().getHouseNumber());
		branchAddress.setHouseName(branchDTO.getAddressDTO().getHouseName().toUpperCase());
		branchAddress.setStreetName(branchDTO.getAddressDTO().getStreetName().toUpperCase());
		branchAddress.setCityName(branchDTO.getAddressDTO().getCityName().toUpperCase());
		branch.setAddress(branchAddress);
		log.info(ApplicationConstant.ADD_NEW_BRANCH);
		return branchRepo.save(branch);
	}

	@Override
	public Branch updateBranchDetails(BranchDTO branchDTO) {
		// ----------- Check weather the branch present or not ----------------//
		Optional<Branch> existingBranchDetails = branchRepo.findById(branchDTO.getBranchId());
		if (!existingBranchDetails.isPresent()) {
			throw new ElementNotFoundException();
		}
		// ----------- Update the existing branch details --------------------//
		Branch updateBranch = branchRepo.findById(branchDTO.getBranchId()).orElse(null);
		updateBranch.setBranchId(branchDTO.getBranchId());
		updateBranch.setBranchName(branchDTO.getBranchName().toUpperCase());
		if (branch.getBranchName().isBlank() || branch.getBranchName().isEmpty()) {
			throw new InputUserException();
		}
		updateBranch.setIfscCode(branchDTO.getIfscCode().toUpperCase());
		if (branch.getIfscCode().isBlank() || branch.getIfscCode().isEmpty()) {
			throw new InputUserException();
		}

		// ----------- Check weather the branch address present or not--------//
		Optional<Address> existingBranchAddress = addressRepo.findById(branchDTO.getAddressDTO().getAddressId());
		if (!existingBranchAddress.isPresent()) {
			throw new ElementNotFoundException();
		}
		// ----------- Update the existing address details --------------------//
		Address updateAddress = addressRepo.findById(branchDTO.getAddressDTO().getAddressId()).orElse(null);
		updateAddress.setAddressId(branchDTO.getAddressDTO().getAddressId());
		updateAddress.setHouseNumber(branchDTO.getAddressDTO().getHouseNumber());
		updateAddress.setHouseName(branchDTO.getAddressDTO().getHouseName().toUpperCase());
		updateAddress.setStreetName(branchDTO.getAddressDTO().getStreetName().toUpperCase());
		updateAddress.setCityName(branchDTO.getAddressDTO().getCityName().toUpperCase());
		updateBranch.setAddress(updateAddress);
		log.info(ApplicationConstant.UPDATE_BRANCH_DETAILS);
		return branchRepo.save(updateBranch);
	}

	@Override
	public List<Branch> fetchAllBranch() {
		List<Branch> existingBranchLists = branchRepo.findAll();
		if (existingBranchLists.isEmpty()) {
			throw new ElementNotFoundException();
		}
		log.info(ApplicationConstant.FETCH_ALL_BRANCH);
		return existingBranchLists;
	}

	@Override
	public List<Manager> fetchAllManagerDetails() {
		List<Manager> existingManagerLists = managerRepo.findAllDetails();
		if (existingManagerLists.isEmpty()) {
			throw new ElementNotFoundException();
		}
		log.info(ApplicationConstant.FETCH_ALL_MANAGER);
		return existingManagerLists;
	}

	@Override
	public Branch findByBranchId(long branchId) {
		Branch existingBrand = branchRepo.findById(branchId).orElse(null);
		if (existingBrand == null) {
			throw new ElementNotFoundException();
		}
		return existingBrand;
	}

	@Override
	public Branch findByName(String branchName) {
		Branch existingBranchDetails = branchRepo.findByBranchName(branchName.toUpperCase()).orElse(null);
		if (existingBranchDetails == null) {
			throw new ElementNotFoundException();
		}
		return existingBranchDetails;
	}

	@Override
	public void deleteBranch(long branchId) {
		Optional<Branch> existingBranchDetails = branchRepo.findById(branchId);
		if (!existingBranchDetails.isPresent()) {
			throw new ElementNotFoundException();
		}
		branchRepo.deleteById(branchId);
	}

	@Override
	public Manager addNewManager(ManagerDTO managerDTO) {
		// ----------- Manager Details --------------//
		manager = new Manager();
		manager.setManagerId(managerDTO.getManagerId());
		manager.setFirstName(managerDTO.getFirstName().toUpperCase());
		if (managerDTO.getFirstName().isBlank() || managerDTO.getFirstName().isEmpty()) {
			throw new InputUserException();
		}
		manager.setLastName(managerDTO.getLastName().toUpperCase());
		if (managerDTO.getLastName().isBlank() || managerDTO.getLastName().isEmpty()) {
			throw new InputUserException();
		}
		manager.setUserName(managerDTO.getUserName());
		if (managerDTO.getUserName().isBlank() || managerDTO.getUserName().isEmpty()) {
			throw new InputUserException();
		}
		manager.setPassword(managerDTO.getPassword());
		if (managerDTO.getPassword().isBlank() || managerDTO.getPassword().isEmpty()) {
			throw new InputUserException();
		}
		manager.setUserType(managerDTO.getUserType().toUpperCase());
		if (managerDTO.getUserType().isBlank() || managerDTO.getUserType().isEmpty()) {
			throw new InputUserException();
		}
		// ------- Manager Address Details ----------//
		managerHomeAddress = new Address();
		managerHomeAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
		managerHomeAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
		managerHomeAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
		managerHomeAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
		managerHomeAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
		manager.setAddress(managerHomeAddress);
		// ------- Assign existing Branch ----------//
		Optional<Branch> existingBranch = branchRepo.findById(managerDTO.getBranchId());
		if (!existingBranch.isPresent()) {
			throw new ElementNotFoundException();
		}
		manager.setBranchId(managerDTO.getBranchId());
		log.info(ApplicationConstant.ADD_NEW_MANAGER);
		return managerRepo.save(manager);
	}

	@Override
	public Manager updateManagerDetails(ManagerDTO managerDTO) {
		// ----- Check entered manager id Present or not ------//
		Optional<Manager> existingManager = managerRepo.findById(managerDTO.getManagerId());
		if (!existingManager.isPresent()) {
			throw new ElementNotFoundException();
		}
		Manager updateManagerDetails = managerRepo.findById(managerDTO.getManagerId()).orElse(null);
		updateManagerDetails.setManagerId(managerDTO.getManagerId());
		updateManagerDetails.setFirstName(managerDTO.getFirstName().toUpperCase());
		updateManagerDetails.setLastName(managerDTO.getLastName().toUpperCase());
		updateManagerDetails.setUserName(managerDTO.getUserName());
		updateManagerDetails.setPassword(managerDTO.getPassword());
		updateManagerDetails.setUserType(managerDTO.getUserType().toUpperCase());

		// ----- Check entered address id Present or not ------//
		Optional<Address> existingAddressDetails = addressRepo.findById(managerDTO.getAddressDTO().getAddressId());
		if (!existingAddressDetails.isPresent()) {
			throw new ElementNotFoundException();
		}

		Address updateExistingAddress = addressRepo.findById(managerDTO.getAddressDTO().getAddressId()).orElse(null);
		updateExistingAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
		updateExistingAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
		updateExistingAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
		updateExistingAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
		updateExistingAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
		updateManagerDetails.setAddress(updateExistingAddress);

		// ----- Check entered branch id Present or not ------//
		Optional<Branch> existingBrand = branchRepo.findById(managerDTO.getBranchId());
		if (!existingBrand.isPresent()) {
			throw new ElementNotFoundException();
		}
		updateManagerDetails.setBranchId(managerDTO.getBranchId());
		return managerRepo.save(updateManagerDetails);

	}

	@Override
	public List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists) {
		List<Manager> newManagerLists = new ArrayList<>();
		// -------------- Custom Validation -------------------//
		for (ManagerDTO newManagerDetails : managerDTOLists) {
			if (newManagerDetails.getFirstName().isBlank() || newManagerDetails.getFirstName().isEmpty()) {
				throw new InputUserException();
			}
			if (newManagerDetails.getLastName().isBlank() || newManagerDetails.getLastName().isEmpty()) {
				throw new InputUserException();
			}
			if (newManagerDetails.getUserName().isBlank() || newManagerDetails.getUserName().isEmpty()) {
				throw new InputUserException();
			}
			if (newManagerDetails.getPassword().isBlank() || newManagerDetails.getPassword().isEmpty()) {
				throw new InputUserException();
			}
			if (newManagerDetails.getUserType().isBlank() || newManagerDetails.getUserType().isEmpty()) {
				throw new InputUserException();
			}
		}

		for (ManagerDTO newManagerDetails : managerDTOLists) {
			// -------------- add new manager details -------------------//
			Manager managerDetails = new Manager();
			managerDetails.setManagerId(newManagerDetails.getManagerId());
			managerDetails.setFirstName(newManagerDetails.getFirstName().toUpperCase());
			managerDetails.setLastName(newManagerDetails.getLastName().toUpperCase());
			managerDetails.setUserName(newManagerDetails.getUserName());
			managerDetails.setPassword(newManagerDetails.getPassword());
			managerDetails.setUserType(newManagerDetails.getUserType().toUpperCase());

			// ------------- add manager home address details --------------//
			Address newManagerAddress = new Address();
			newManagerAddress.setAddressId(newManagerDetails.getAddressDTO().getAddressId());
			newManagerAddress.setHouseNumber(newManagerDetails.getAddressDTO().getHouseNumber());
			newManagerAddress.setHouseName(newManagerDetails.getAddressDTO().getHouseName().toUpperCase());
			newManagerAddress.setStreetName(newManagerDetails.getAddressDTO().getStreetName().toUpperCase());
			newManagerAddress.setCityName(newManagerDetails.getAddressDTO().getCityName().toUpperCase());
			managerDetails.setAddress(newManagerAddress);

			// ----------- assing branch to the manager -----------//
			Optional<Branch> existingBranch = branchRepo.findById(newManagerDetails.getBranchId());
			if (!existingBranch.isPresent()) {
				throw new ElementNotFoundException();
			}
			managerDetails.setBranchId(newManagerDetails.getBranchId());

			managerRepo.save(managerDetails);
			newManagerLists.add(managerDetails);
		}
		return newManagerLists;
	}

	@Override
	public List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists) {
		List<Manager> updateListsofManagerDetails = new ArrayList<>();
		for (ManagerDTO eachManagerDetails : managerDTOLists) {
			Optional<Manager> existingManager = managerRepo.findById(eachManagerDetails.getManagerId());
			if (!existingManager.isPresent()) {
				throw new ElementNotFoundException();
			}

			Optional<Address> existingManagerAddress = addressRepo
					.findById(eachManagerDetails.getAddressDTO().getAddressId());
			if (existingManagerAddress.isPresent()) {
				throw new ElementNotFoundException();
			}

			Optional<Branch> existingBranch = branchRepo.findById(eachManagerDetails.getBranchId());
			if (!existingBranch.isPresent()) {
				throw new ElementNotFoundException();
			}

			if (eachManagerDetails.getFirstName().isBlank() || eachManagerDetails.getFirstName().isEmpty()) {
				throw new InputUserException();
			}
			if (eachManagerDetails.getLastName().isBlank() || eachManagerDetails.getLastName().isEmpty()) {
				throw new InputUserException();
			}
			if (eachManagerDetails.getUserName().isBlank() || eachManagerDetails.getUserName().isEmpty()) {
				throw new InputUserException();
			}
			if (eachManagerDetails.getPassword().isBlank() || eachManagerDetails.getPassword().isEmpty()) {
				throw new InputUserException();
			}
			if (eachManagerDetails.getUserType().isBlank() || eachManagerDetails.getUserType().isEmpty()) {
				throw new InputUserException();
			}
		}

		for (ManagerDTO eachManagerDetails : managerDTOLists) {
			Manager updateManagerDetails = managerRepo.findById(eachManagerDetails.getManagerId()).orElse(null);
			updateManagerDetails.setManagerId(updateManagerDetails.getManagerId());
			updateManagerDetails.setFirstName(updateManagerDetails.getFirstName().toUpperCase());
			updateManagerDetails.setLastName(updateManagerDetails.getLastName().toUpperCase());
			updateManagerDetails.setUserName(updateManagerDetails.getUserName());
			updateManagerDetails.setPassword(updateManagerDetails.getPassword());
			updateManagerDetails.setUserType(updateManagerDetails.getUserType().toUpperCase());

			// ------------- add manager home address details --------------//
			Address updateAddress = addressRepo.findById(eachManagerDetails.getAddressDTO().getAddressId())
					.orElse(null);
			updateAddress.setAddressId(eachManagerDetails.getAddressDTO().getAddressId());
			updateAddress.setHouseNumber(eachManagerDetails.getAddressDTO().getHouseNumber());
			updateAddress.setHouseName(eachManagerDetails.getAddressDTO().getHouseName().toUpperCase());
			updateAddress.setStreetName(eachManagerDetails.getAddressDTO().getStreetName().toUpperCase());
			updateAddress.setCityName(eachManagerDetails.getAddressDTO().getCityName().toUpperCase());
			updateManagerDetails.setAddress(updateAddress);

			// ----------- assing branch to the manager -----------//
			Branch updateBranch = branchRepo.findById(eachManagerDetails.getBranchId()).orElse(null);
			updateBranch.setBranchId(eachManagerDetails.getBranchId());
			updateManagerDetails.setBranchId(updateBranch.getBranchId());

			managerRepo.save(updateManagerDetails);
			updateListsofManagerDetails.add(updateManagerDetails);
		}

		return updateListsofManagerDetails;
	}

	@Override
	public Manager findByManagerId(long managerId) {
		Manager existingManger = managerRepo.findById(managerId).orElse(null);
		if (existingManger == null) {
			throw new ElementNotFoundException();
		}
		return existingManger;
	}

	@Override
	public Manager findByFirstNameAndLastName(String firstName, String lastName) {
		Manager existingManager = managerRepo.findByFirstNameAndLastName(firstName, lastName);
		if (existingManager == null) {
			throw new ElementNotFoundException();
		}
		return existingManager;
	}

	@Override
	public List<Manager> fetchAllManagerListsInAlphabeticalOrder() {
		List<Manager> existingManagerLists = managerRepo.findAll();
		if (existingManagerLists.isEmpty()) {
			throw new ElementNotFoundException();
		}
		List<Manager> alphabeticalOrderManagerLists = existingManagerLists.stream()
				.sorted((e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName())).collect(Collectors.toList());

		return alphabeticalOrderManagerLists;
	}

	@Override
	public Manager findManagerByBranchId(long branchId) {
		Manager existingManager = managerRepo.findByBranchId(branchId);
		if (existingManager == null) {
			throw new ElementNotFoundException();
		}
		return existingManager;
	}

	@Override
	public void deleteManager(long managerId) {
		Manager existingManager = managerRepo.findById(managerId).orElse(null);
		if (existingManager == null) {
			throw new ElementNotFoundException();
		}
		managerRepo.deleteById(managerId);
	}
}
