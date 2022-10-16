package com.xoriant.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.exception.InputUserException;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.Creaditional;
import com.xoriant.bank.model.ErrorCode;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.model.ManagerCredential;
import com.xoriant.bank.repo.AddressRepo;
import com.xoriant.bank.repo.BranchRepo;
import com.xoriant.bank.repo.ManagerCredentialRepo;
import com.xoriant.bank.repo.ManagerRepo;
import com.xoriant.bank.sender.service.ManagerInfoMsgSender;
import com.xoriant.bank.util.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ManagerServiceImplDetails {

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private BranchService branchService;

	@Autowired
	private ManagerInfoMsgSender managerInfoMsgSender;

	@Autowired
	private AddressRepo addressRepo;

	@Value("${retryCount}")
	private int RETRY_COUNT;

	private int retryCount = 0;

	@Value("${waitTime}")
	private int WAIT_TIME;

	@Autowired
	private RuntimeManager runtimeManager;

	@Autowired
	private ManagerCredentialRepo managerCredentialRepo;
	
	@Autowired
	private BranchRepo branchRepo;

	@Transactional
	public Manager addNewManager(ManagerDTO managerDTO) {
		Manager NewManagerDetails = null;
		if (managerDTO != null) {
			// ----------- Manager Details --------------//
			Manager manager = new Manager();
			manager.setManagerId(managerDTO.getManagerId());
			manager.setFirstName(managerDTO.getFirstName().toUpperCase());
			if (managerDTO.getFirstName().isBlank() || managerDTO.getFirstName().isEmpty()) {
				log.info("Manager first name is null. No need to proceed further");
				return null;
			}
			manager.setLastName(managerDTO.getLastName().toUpperCase());
			if (managerDTO.getLastName().isBlank() || managerDTO.getLastName().isEmpty()) {
				log.info("Manager last name is null. No need to proceed further.");
				return null;
			}

			ManagerCredential credential = new ManagerCredential();

			credential.setUserName(managerDTO.getCredentialDTO().getUserName());
			if (managerDTO.getCredentialDTO().getUserName().isBlank()
					|| managerDTO.getCredentialDTO().getUserName().isEmpty()) {
				log.info("Manager username is null. No need to proceed further.");
			}
			credential.setPassword(managerDTO.getCredentialDTO().getPassword());

			if (managerDTO.getCredentialDTO().getPassword().isBlank()
					|| managerDTO.getCredentialDTO().getPassword().isEmpty()) {
				log.info("Manager password is null. No need to proceed further.");
			}

			manager.setCredential(credential);
			log.info("Manager creaditional added");

			manager.setUserType(managerDTO.getUserType().toUpperCase());
			if (managerDTO.getUserType().isBlank() || managerDTO.getUserType().isEmpty()) {
				log.info("ManagerServiceImplDetails - user type not defined. No need to proceed further.");
				return null;
			}
			log.info("Manager Basic details added");
			// ------- Manager Address Details ----------//

			Address managerHomeAddress = new Address();
			managerHomeAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
			managerHomeAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
			managerHomeAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
			managerHomeAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
			managerHomeAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
			manager.setAddress(managerHomeAddress);
			log.info("Manager home address details added");
			// ------- Assign existing Branch ----------//
			Branch existingBranch = branchService.findByBranchId(managerDTO.getBranchId());
			manager.setBranchId(existingBranch.getBranchId());
			log.info("Branch assinged to the manager.");
			log.info(ApplicationConstant.ADD_NEW_MANAGER);
			NewManagerDetails = managerRepo.save(manager);
			String managerDetails = manager.toString();
			publishMessageToQueue(managerDetails, retryCount);
		} else {
			log.info("ManagerDTO is null. No need to proceed further.");
			return null;
		}
		return NewManagerDetails;

	}

	public Manager updateManagerDetails(ManagerDTO managerDTO) {
		Manager updatedManagerDTO=null;
		// ----- Check entered manager id Present or not ------//
		boolean existingManagerResult = findManagerById(managerDTO.getManagerId());
		if (existingManagerResult == false) {
			log.info("Entered manager id " + managerDTO.getManagerId()
					+ " not present in system. No need to proceed further.");
			return null;
		}
		log.info("Entered manager id " + managerDTO.getManagerId() + " present in system");
		Manager updateManagerDetails = managerRepo.findById(managerDTO.getManagerId()).orElse(null);
		updateManagerDetails.setManagerId(managerDTO.getManagerId());
		updateManagerDetails.setFirstName(managerDTO.getFirstName().toUpperCase());
		updateManagerDetails.setLastName(managerDTO.getLastName().toUpperCase());

		// ------- Check entered login id present or not --------//
		boolean verifyExistingDetails = findManagerCredentialDetails(managerDTO.getCredentialDTO().getId(),
				managerDTO.getCredentialDTO().getUserName(), managerDTO.getCredentialDTO().getPassword());
		if (verifyExistingDetails == false) {
			log.info("Manager Creaditional not matched. No need to proceed further.");
			return null;
		}
		ManagerCredential updateManagerCredential = new ManagerCredential();
		updateManagerCredential.setId(managerDTO.getCredentialDTO().getId());
		updateManagerCredential.setUserName(managerDTO.getCredentialDTO().getUserName());
		updateManagerCredential.setPassword(managerDTO.getCredentialDTO().getPassword());
		updateManagerDetails.setCredential(updateManagerCredential);
		log.info("Manager Creaditional updated");

		// ----- Check entered address id Present or not ------//
		Optional<Address> existingAddressDetails = addressRepo.findById(managerDTO.getAddressDTO().getAddressId());
		if (!existingAddressDetails.isPresent()) {
			log.info("Manager address not found. No need to further proceed.");
			return null;
		}
		Address updateExistingAddress = addressRepo.findById(managerDTO.getAddressDTO().getAddressId()).orElse(null);
		updateExistingAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
		updateExistingAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
		updateExistingAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
		updateExistingAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
		updateExistingAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
		updateManagerDetails.setAddress(updateExistingAddress);

//				// ----- Check entered branch id Present or not ------//
		boolean existingBrand = findBranchById(managerDTO.getBranchId());
		if (existingBrand==false) {
			log.info("Entered Branch id not present in system. No need to proceed further.");
		}
		updateManagerDetails.setBranchId(managerDTO.getBranchId());
		updatedManagerDTO=managerRepo.save(updateManagerDetails);
		String updatedManagerDetails=updatedManagerDTO.toString();
		publishMessageToQueue(updatedManagerDetails,0);
		return updatedManagerDTO;
	}

	private boolean findBranchById(long branchId) {
		Optional<Branch> existingBranch=branchRepo.findById(branchId);
		if(!existingBranch.isPresent())
			return false;
		return true;
	}

	private boolean findManagerCredentialDetails(long id, String userName, String password) {
		Optional<ManagerCredential> existingCreaditional = managerCredentialRepo.findById(id);
		if (!existingCreaditional.isPresent()) {
			log.info("Manager id not present in system. No need to proceed further.");
			return false;
		}
		if (!existingCreaditional.get().getUserName().equals(userName)) {
			log.info("Manager username not mathced. No need to proceed further.");
			return false;
		}
		if(!existingCreaditional.get().getPassword().equals(password)) {
			log.info("Manager password not mathced. No need to proceed further.");
			return false;
		}
		log.info("manager creatiditional matched");
		return true;
	}

	private boolean findManagerById(long managerId) {
		Optional<Manager> existingManager = managerRepo.findById(managerId);
		if (!existingManager.isPresent())
			return false;
		else
			return true;
	}

	private void publishMessageToQueue(String managerDetails, int retryCount) {
		try {
			managerInfoMsgSender.managerDetails(managerDetails);
		} catch (Exception e) {
			log.error("Exception occured while sending msg to queue", e);
			try {
				if (retryCount < RETRY_COUNT) {
					Thread.sleep(retryCount * WAIT_TIME);
					retryCount++;
					log.info("Routing message again with count " + retryCount);
					publishMessageToQueue(managerDetails, retryCount);
				} else {
					log.info("Exception occured while sending msg to queue");
					log.info("Maximun retrycount has reached  " + retryCount);
					runtimeManager.getErrorController(ErrorCode.PUBLISHING_MSG_TO_QUEUE_FAILED);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
