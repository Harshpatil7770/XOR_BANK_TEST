package com.xoriant.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.exception.InputUserException;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.ErrorCode;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.model.ManagerCredential;
import com.xoriant.bank.repo.AddressRepo;
import com.xoriant.bank.repo.BranchRepo;
import com.xoriant.bank.repo.ManagerCredentialRepo;
import com.xoriant.bank.repo.ManagerRepo;
import com.xoriant.bank.sender.service.BranchInfoMsgService;
import com.xoriant.bank.util.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BranchServiceImplDetails {

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private ManagerCredentialRepo managerCredentialRepo;

	@Autowired(required = false)
	private RuntimeManager runtimeManager;

	@Autowired
	private BranchInfoMsgService branchInfoMsgService;

	private final int RETRY_COUNT = 3;
	private final int WAIT_TIME = 3000;

	private Branch branch;
	private Address branchAddress;

	private Manager manager;
	private Address managerHomeAddress;
	private ManagerCredential credential;

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean addNewBranchDetails(BranchDTO branchDTO) throws Exception {
		if (branchDTO != null) {
			// ----------- Branch Details --------------//
			branch = new Branch();
			branch.setBranchId(branchDTO.getBranchId());
			branch.setBranchName(branchDTO.getBranchName().toUpperCase());
			if (branch.getBranchName().isBlank() || branch.getBranchName().isEmpty()) {
				log.info("Branch name is null. No need to proceed further.");
				return false;
			}
			branch.setIfscCode(branchDTO.getIfscCode());
			if (branch.getIfscCode().isBlank() || branch.getIfscCode().isEmpty()) {
				log.info("IFSC Code is null. No need to proceed further.");
				return false;
			}
			// ------- Branch Address Details ----------//
			log.info("Branch id,name,ifsc code started adding ....");
			branchAddress = new Address();
			branchAddress.setAddressId(branchDTO.getAddressDTO().getAddressId());
			branchAddress.setHouseNumber(branchDTO.getAddressDTO().getHouseNumber());
			branchAddress.setHouseName(branchDTO.getAddressDTO().getHouseName().toUpperCase());
			branchAddress.setStreetName(branchDTO.getAddressDTO().getStreetName().toUpperCase());
			branchAddress.setCityName(branchDTO.getAddressDTO().getCityName().toUpperCase());
			branch.setAddress(branchAddress);
			log.info("Branch address details adding ...");
			log.info(ApplicationConstant.ADD_NEW_BRANCH);
			branchRepo.save(branch);
			int retryCount = 0;
			String msgDetails=branch.toString();
			publishToQueue(msgDetails, retryCount);
			return true;
		} else {
			log.info("branchDTO is null. No need to further proceed.");
			return false;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean updateExistingBranch(BranchDTO branchDTO) {
		if (branchDTO != null) {
			// ----------- Check weather the branch present or not ----------------//
			Optional<Branch> existingBranchDetails = branchRepo.findById(branchDTO.getBranchId());
			if (!existingBranchDetails.isPresent()) {
				log.info("Entered Brand id not present in database. No need to proceed further");
				return false;
			}
			log.info("Entered branch_id is present moving further ....");
			// ----------- Update the existing branch details --------------------//
			Branch updateBranch = branchRepo.findById(branchDTO.getBranchId()).orElse(null);
			updateBranch.setBranchId(branchDTO.getBranchId());
			updateBranch.setBranchName(branchDTO.getBranchName().toUpperCase());
			if (branchDTO.getBranchName().isBlank() || branchDTO.getBranchName().isEmpty()) {
				log.info("Branch Name is Empty. No need to proceed further.");
				return false;
			}
			updateBranch.setIfscCode(branchDTO.getIfscCode().toUpperCase());
			if (branchDTO.getIfscCode().isBlank() || branchDTO.getIfscCode().isEmpty()) {
				log.info("IFSC Code is empty. No need to proceed further.");
				return false;
			}

			// ----------- Check weather the branch address present or not--------//
			Optional<Address> existingBranchAddress = addressRepo.findById(branchDTO.getAddressDTO().getAddressId());
			if (!existingBranchAddress.isPresent()) {
				log.info("Entered address id not present in database. No need to proceed futher.");
				return false;
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
			branchRepo.save(updateBranch);
			int retryCount = 0;
			String msgDetails=updateBranch.toString();
			publishToQueue(msgDetails, retryCount);
		} else {
			log.info("BranchDTO is empty. No need to proceed further");
		}
		return true;
	}

	public void deleteExistingBranch(String msgDetails) {
		int retryCount=0;
		publishToQueue(msgDetails,retryCount);
	}
	/*
	 * if you are sending msg to queue and exception occured at runtime like lost mq
	 * connection or anything need to retry the logic with thread sleep. But if you
	 * retrying method from starting point of unit of work you don't need to wait or
	 * don't need to use thread sleep.
	 */
	private void publishToQueue(String msgDetails, int retryCount) {
		try {
			branchInfoMsgService.publishMessageToQueue(msgDetails);
		} catch (Exception e) {
			log.info("Exception occured while publishing message to queue " + e);
			if (retryCount < RETRY_COUNT) {
				try {
					Thread.sleep(retryCount * WAIT_TIME);
					retryCount++;
					log.info("Routing message again for count  " + retryCount);
					publishToQueue(msgDetails, retryCount);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else {
				log.info("Exception occured while publishing message to queue " + e);
				log.info("Maximum retry count limit has reached " + retryCount);
				runtimeManager.getErrorController(ErrorCode.NEW_BRANCH_ADDITION_FAILED);
			}
		}
	}

}
