package com.xoriant.bank.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.dto.ManagerDTO;
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
import com.xoriant.bank.util.ApplicationConstant;

@Service
@Component
public class BranchServiceImpl implements BranchService {

	private final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

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
	private BranchServiceImplDetails branchServiceImplDetails;

	private final int RETRY_COUNT = 3;
	private final int WAIT_TIME = 3000;
	int retryCount = 0;

	private Branch branch;
	private Address branchAddress;

	private Manager manager;
	private Address managerHomeAddress;
	private ManagerCredential credential;

	Map<Long, Queue<BranchDTO>> initialStoredMap;

	ObjectMapper objectMapper;

	// @PostConstruct method level annotations. execute method only once
	// after the initilization of bean
	@PostConstruct
	public void init() {
		initialStoredMap = new ConcurrentHashMap<>();
		objectMapper = new ObjectMapper();
	}

	public void removeDataFromMap(BranchDTO branchDTO) {
		logger.info("Processing of message is completed. Now removing from map.");
		synchronized (initialStoredMap) {
			Long key = branchDTO.getBranchId();
			if (key != null && initialStoredMap.containsKey(key)) {
				logger.info("Checking basic details map for sequence key - {}", key);
				if (initialStoredMap.get(key).isEmpty()) {
					logger.info("key removed from basic details map - {}", key);
					initialStoredMap.remove(key);
				}

			}
		}
	}

	@Override
	public boolean addNewBranch(BranchDTO branchDTO) {
		boolean branchDetails = false;
		try {
			if (branchDTO != null) {
				branchDetails = branchServiceImplDetails.addNewBranchDetails(branchDTO);
			} else {
				logger.info("branchDTO is null. no need to further proceed.");
				branchDetails = false;
			}
			return branchDetails;
		} catch (Exception e) {
			logger.error("BranchServiceImplDetails- Expection occured while adding new branch.");
			throw new RuntimeException(e);
		}
	}

//	private boolean addNewBranchDetails(BranchDTO branchDTO) throws Exception {
//		boolean branchDetails = false;
////		try {
//		branchDetails = branchServiceImplDetails.addNewBranchDetails(branchDTO);
////		} catch (Exception e) {
////			branchDetails = false;
////			log.info("Exception occured while adding new Branch details " + e);
////			runtimeManager.getErrorController(ErrorCode.NEW_BRANCH_ADDITION_FAILED);
////		}
//		return branchDetails;
//	}

	@Override
	public boolean updateBranchDetails(BranchDTO branchDTO) {
		boolean branchDetails = false;
		if (branchDTO != null) {
			initialStoredMap.put(branchDTO.getBranchId(), new LinkedList<BranchDTO>());
//			branchDetails = updateExistingBranch(branchDTO, 0);
			removeDataFromMap(branchDTO);
		} else {
			logger.info("branchDTO is null. No need to proceed further");
			branchDetails = false;
		}
		return branchDetails;
	}

	private boolean updateExistingBranch(BranchDTO branchDTO, int retryCount) {
		boolean branchDetails = false;
		try {
			branchDetails = branchServiceImplDetails.updateExistingBranch(branchDTO);
		} catch (Exception e) {
			logger.error("BranchServiceImpl - Exception occured while updating branch details ", e);
			if (retryCount < RETRY_COUNT) {
				retryCount++;
				logger.info("BranchServiceImpl - Retring routing of message count {}", retryCount);
				updateExistingBranch(branchDTO, retryCount);
			} else {
				logger.info("BranchServiceImpl - Exception occured while updating branch details ", e);
				logger.info("BranchServiceImpl - Maximum retrying routing of message count is reached {}", retryCount);
				runtimeManager.getErrorController(ErrorCode.EXISTING_BRANCH_UPDATION_FAILED);
			}
//			branchDetails = false;
//			logger.info("Exception occured while updating existing branch details " + e);
//			runtimeManager.getErrorController(ErrorCode.EXISTING_BRANCH_UPDATION_FAILED);
		}
		return branchDetails;
	}

//	@Override
//	@Cacheable(value = "adminServiceImplCache")
//	public List<Branch> fetchAllBranch() {
//		try {
//		List<Branch> existingBranchLists = branchRepo.findAll();
//		if (existingBranchLists.isEmpty()) {
//			throw new ElementNotFoundException();
//		}
//		logger.info(ApplicationConstant.FETCH_ALL_BRANCH);
//		return existingBranchLists;
//		}catch(Exception e) {
//			logger.error("BranchServiceImpl - Exception ocured while fetching data from branch",e);
//			try {
//				retryCount++;
//				Thread.sleep(retryCount* WAIT_TIME);
//				logger.info("BranchServiceImpl - Retrying routing of message count {}",retryCount);
//				fetchAllBranch(retryCount);
//			}catch(Exception ex) {
//				
//			}
//		}
//	}

	@Override
	@Cacheable(value = "adminServiceImplCache", key = "#branchId")
	public Branch findByBranchId(long branchId, int retryCount) {
		Branch existingBrand = null;
		try {
			existingBrand = branchRepo.findById(branchId).orElse(null);
			if (existingBrand == null) {
				logger.info("Entered Branch Id :: " + branchId
						+ "  details not available in system. No need to proceed further.");
				return null;
			}
			logger.info("Branch details found for the entered branchId " + branchId);
		} catch (Exception e) {
			logger.error("BranchServiceImpl - Exception occured while fetching data from db ", e);
			if (retryCount < RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * WAIT_TIME);
					logger.info("BranchServiceImpl - Retring routing of message count {}", retryCount);
					findByBranchId(branchId, retryCount);
				} catch (Exception ex) {
					logger.info("BranchServiceImpl - Exception occured while fetching data from db ", e);
					logger.info("BranchServiceImpl - Maximun Retring routing of message count {} is reached",
							retryCount);
					runtimeManager.getErrorController(ErrorCode.FAILED_FETHCING_BRANCH_DETAILS);
				}
			}
		}
		logger.debug("Branch details found for the entered branchId ", existingBrand);
		return existingBrand;
	}

	@Override
	@Cacheable(value = "adminServiceImplCache", key = "#branchName")
	public Branch findBranchByName(String branchName, int retryCount) {
		Branch existingBranchDetails = null;
		try {
			existingBranchDetails = branchRepo.findByBranchName(branchName.toUpperCase()).orElse(null);
			if (existingBranchDetails == null) {
				logger.info("Entered branch name  " + branchName + " . Branch details not avaible for this name."
						+ "No Need to proceed further.");
				return null;
			}
			logger.info("Branch details avaliable for the entered branchName " + branchName);
		} catch (Exception e) {
			logger.error("BranchServiceImpl - Exception occured while fetching the data", e);
			if (retryCount < RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * WAIT_TIME);
					logger.info("BranchServiceImpl - Retrying routing of message count {}", retryCount);
					findBranchByName(branchName, retryCount);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("BranchServiceImpl - Exception occured while fetching the data", e);
				logger.info("BranchServiceImpl - maximun retrying routing is reached {}", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETHCING_BRANCH_DETAILS);
			}
		}
		return existingBranchDetails;

	}

	@Override
	/*
	 * once you delete any branchId=21 and after that also you called this method so
	 * it will give same response, so no need to write cacheable here
	 */
//	@Cacheable(value = "adminServiceImplCache", key = "#branchId")
	@Transactional
	public boolean deleteBranch(long branchId, int retryCount) {
		try {
			Branch existingBranch = branchRepo.findById(branchId).orElse(null);
			if (existingBranch == null) {
				logger.info("Entered branch id is not present in database. No need to further Proceed.");
				return false;
			}
			branchRepo.deleteById(branchId);
			logger.info("Deleted Existing Branch Succesfully -  BRANCH_ID {}", branchId);
		} catch (Exception e) {
			logger.error("BranchServiceImpl - Exception occured while fetching data from db ", e);
			try {
				if (retryCount < RETRY_COUNT) {
					retryCount++;
					Thread.sleep(retryCount * WAIT_TIME);
					logger.info("BranchServiceImpl - Retrying routing message count {}", retryCount);
					deleteBranch(branchId, retryCount);
				} else {
					logger.info("BranchServiceImpl - Exception occured while fetching data from db ", e);
					logger.info("BranchServiceImpl - Maximun Retrying routing message count {} is reached", retryCount);
					runtimeManager.getErrorController(ErrorCode.DELETION_OF_BRANCH_FAILED);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}

	@Override
	@Cacheable(value = "adminServiceImplCache")
	public List<Branch> findAllBranchesWithAddressDetails(int retryCount) {
		List<Branch> existingBranchDetails = null;
		try {
			existingBranchDetails = branchRepo.findAllBranchesWithAddressDetails();
			if (existingBranchDetails == null) {
				logger.info("Branch Details not found in database. No need to proceed further.");
				return null;
			}
			logger.info("Branch and their address details present in database");
		} catch (Exception e) {
			logger.error("BranchServiceImpl - Exception occured while fetching data.");
			if (retryCount < RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * WAIT_TIME);
					logger.info("BranchServiceImpl - Retrying routing of message count {}", retryCount);
					findAllBranchesWithAddressDetails(retryCount);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			} else {
				logger.info("BranchServiceImpl - Exception occured while fetching data.");
				logger.info("BranchServiceImpl - Maximun retrying routing count reached {}", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETHCING_BRANCH_DETAILS);
			}
		}
		return existingBranchDetails;
	}

//	@Override
//	public Manager addNewManager(ManagerDTO managerDTO) {
//		// ----------- Manager Details --------------//
//		manager = new Manager();
//		manager.setManagerId(managerDTO.getManagerId());
//		manager.setFirstName(managerDTO.getFirstName().toUpperCase());
//		if (managerDTO.getFirstName().isBlank() || managerDTO.getFirstName().isEmpty()) {
//			throw new InputUserException();
//		}
//		manager.setLastName(managerDTO.getLastName().toUpperCase());
//		if (managerDTO.getLastName().isBlank() || managerDTO.getLastName().isEmpty()) {
//			throw new InputUserException();
//		}
//
//		credential = new ManagerCredential();
//
//		credential.setUserName(managerDTO.getCredentialDTO().getUserName());
//		if (managerDTO.getCredentialDTO().getUserName().isBlank()
//				|| managerDTO.getCredentialDTO().getUserName().isEmpty()) {
//			throw new InputUserException();
//		}
//		credential.setPassword(managerDTO.getCredentialDTO().getPassword());
//		if (managerDTO.getCredentialDTO().getPassword().isBlank()
//				|| managerDTO.getCredentialDTO().getPassword().isEmpty()) {
//			throw new InputUserException();
//		}
//		manager.setCredential(credential);
//		log.info("Manager creaditional added");
//
//		manager.setUserType(managerDTO.getUserType().toUpperCase());
//		if (managerDTO.getUserType().isBlank() || managerDTO.getUserType().isEmpty()) {
//			throw new InputUserException();
//		}
//		log.info("Manager Basic details added");
//		// ------- Manager Address Details ----------//
//		managerHomeAddress = new Address();
//		managerHomeAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
//		managerHomeAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
//		managerHomeAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
//		managerHomeAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
//		managerHomeAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
//		manager.setAddress(managerHomeAddress);
//		log.info("Manager home address details added");
//		// ------- Assign existing Branch ----------//
////		Optional<Branch> existingBranch = branchRepo.findById(managerDTO.getBranchId());
////		if (!existingBranch.isPresent()) {
////			throw new ElementNotFoundException();
////		}
//		Branch existingBranchResponse = findByBranchId(managerDTO.getBranchId());
//		manager.setBranchId(existingBranchResponse.getBranchId());
//		log.info("Branch assinged to the manager.");
//		log.info(ApplicationConstant.ADD_NEW_MANAGER);
//		return managerRepo.save(manager);
//	}
//
//	@Override
//	@Cacheable(value = "adminServiceImplCache")
//	public List<Manager> fetchAllManagerDetails() {
//		List<Manager> existingManagerLists = managerRepo.findAllManagerWithTheirBranchDetails();
//		if (existingManagerLists.isEmpty()) {
//			log.info("Manager details not found in system");
//			throw new ElementNotFoundException();
//		}
//		log.info("Manager details found in system");
//		return existingManagerLists;
//	}

//	@Override
//	public boolean updateManagerDetails(ManagerDTO managerDTO) {
//		// ----- Check entered manager id Present or not ------//
//		boolean existingManagerResult = findManagerById(managerDTO.getManagerId());
//		if (existingManagerResult == false) {
//			log.info("Entered manager id " + managerDTO.getManagerId() + " not present in system");
//			return false;
//		}
//		log.info("Entered manager id " + managerDTO.getManagerId() + " present in system");
//		Manager updateManagerDetails = managerRepo.findById(managerDTO.getManagerId()).orElse(null);
//		updateManagerDetails.setManagerId(managerDTO.getManagerId());
//		updateManagerDetails.setFirstName(managerDTO.getFirstName().toUpperCase());
//		updateManagerDetails.setLastName(managerDTO.getLastName().toUpperCase());
//
//		// ------- Check entered login id present or not --------//
//		boolean verifyExistingDetails = findManagerCredentialDetails(managerDTO.getCredentialDTO().getId(),
//				managerDTO.getCredentialDTO().getUserName(), managerDTO.getCredentialDTO().getPassword());
//		if (verifyExistingDetails == false) {
//			log.info("Manager Creaditional not matched.");
//			return false;
//		}
//		ManagerCredential updateManagerCredential = new ManagerCredential();
//		updateManagerCredential.setId(managerDTO.getCredentialDTO().getId());
//		updateManagerCredential.setUserName(managerDTO.getCredentialDTO().getUserName());
//		updateManagerCredential.setPassword(managerDTO.getCredentialDTO().getPassword());
//		updateManagerDetails.setCredential(updateManagerCredential);
//		log.info("Manager Creaditional updated");
//
//		// ----- Check entered address id Present or not ------//
//		Optional<Address> existingAddressDetails = addressRepo.findById(managerDTO.getAddressDTO().getAddressId());
//		if (!existingAddressDetails.isPresent()) {
//			throw new ElementNotFoundException();
//		}
//
//		Address updateExistingAddress = addressRepo.findById(managerDTO.getAddressDTO().getAddressId()).orElse(null);
//		updateExistingAddress.setAddressId(managerDTO.getAddressDTO().getAddressId());
//		updateExistingAddress.setHouseNumber(managerDTO.getAddressDTO().getHouseNumber());
//		updateExistingAddress.setHouseName(managerDTO.getAddressDTO().getHouseName().toUpperCase());
//		updateExistingAddress.setStreetName(managerDTO.getAddressDTO().getStreetName().toUpperCase());
//		updateExistingAddress.setCityName(managerDTO.getAddressDTO().getCityName().toUpperCase());
//		updateManagerDetails.setAddress(updateExistingAddress);
//
////		// ----- Check entered branch id Present or not ------//
//		Optional<Branch> existingBrand = branchRepo.findById(managerDTO.getBranchId());
//		if (!existingBrand.isPresent()) {
//			throw new ElementNotFoundException();
//		}
//		updateManagerDetails.setBranchId(managerDTO.getBranchId());
//		managerRepo.save(updateManagerDetails);
//		return false;
//	}
//
//	private boolean findManagerCredentialDetails(long id, String username, String password) {
//		Optional<ManagerCredential> existingManager = managerCredentialRepo.findById(id);
//		if (!existingManager.isPresent()) {
//			log.info("Entered Login Id " + id + " not present in the system");
//			return false;
//		}
//		if (existingManager.get().getUserName().compareTo(username) < 0) {
//			log.info("Entered username is not matched to existing username");
//			return false;
//		}
//		if (existingManager.get().getPassword().compareTo(password) < 0) {
//			log.info("Entered password is not matched to existing password");
//			return false;
//		}
//		log.info("Manager creaditional matches");
//		return true;
//	}
//
//	@Override
//	public List<Manager> addNewListsOfManager(List<ManagerDTO> managerDTOLists) {
////		List<Manager> newManagerLists = new ArrayList<>();
////		// -------------- Custom Validation -------------------//
////		for (ManagerDTO newManagerDetails : managerDTOLists) {
////			if (newManagerDetails.getFirstName().isBlank() || newManagerDetails.getFirstName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (newManagerDetails.getLastName().isBlank() || newManagerDetails.getLastName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (newManagerDetails.getUserName().isBlank() || newManagerDetails.getUserName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (newManagerDetails.getPassword().isBlank() || newManagerDetails.getPassword().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (newManagerDetails.getUserType().isBlank() || newManagerDetails.getUserType().isEmpty()) {
////				throw new InputUserException();
////			}
////		}
////
////		for (ManagerDTO newManagerDetails : managerDTOLists) {
////			// -------------- add new manager details -------------------//
////			Manager managerDetails = new Manager();
////			managerDetails.setManagerId(newManagerDetails.getManagerId());
////			managerDetails.setFirstName(newManagerDetails.getFirstName().toUpperCase());
////			managerDetails.setLastName(newManagerDetails.getLastName().toUpperCase());
////			managerDetails.setUserName(newManagerDetails.getUserName());
////			managerDetails.setPassword(newManagerDetails.getPassword());
////			managerDetails.setUserType(newManagerDetails.getUserType().toUpperCase());
////
////			// ------------- add manager home address details --------------//
////			Address newManagerAddress = new Address();
////			newManagerAddress.setAddressId(newManagerDetails.getAddressDTO().getAddressId());
////			newManagerAddress.setHouseNumber(newManagerDetails.getAddressDTO().getHouseNumber());
////			newManagerAddress.setHouseName(newManagerDetails.getAddressDTO().getHouseName().toUpperCase());
////			newManagerAddress.setStreetName(newManagerDetails.getAddressDTO().getStreetName().toUpperCase());
////			newManagerAddress.setCityName(newManagerDetails.getAddressDTO().getCityName().toUpperCase());
////			managerDetails.setAddress(newManagerAddress);
////
////			// ----------- assing branch to the manager -----------//
////			Optional<Branch> existingBranch = branchRepo.findById(newManagerDetails.getBranchId());
////			if (!existingBranch.isPresent()) {
////				throw new ElementNotFoundException();
////			}
////			managerDetails.setBranchId(newManagerDetails.getBranchId());
////
////			managerRepo.save(managerDetails);
////			newManagerLists.add(managerDetails);
////		}
////		return newManagerLists;
//		return null;
//	}
//
//	@Override
//	public List<Manager> updateListsOfManager(List<ManagerDTO> managerDTOLists) {
////		List<Manager> updateListsofManagerDetails = new ArrayList<>();
////		for (ManagerDTO eachManagerDetails : managerDTOLists) {
////			Optional<Manager> existingManager = managerRepo.findById(eachManagerDetails.getManagerId());
////			if (!existingManager.isPresent()) {
////				throw new ElementNotFoundException();
////			}
////
////			Optional<Address> existingManagerAddress = addressRepo
////					.findById(eachManagerDetails.getAddressDTO().getAddressId());
////			if (existingManagerAddress.isPresent()) {
////				throw new ElementNotFoundException();
////			}
////
////			Optional<Branch> existingBranch = branchRepo.findById(eachManagerDetails.getBranchId());
////			if (!existingBranch.isPresent()) {
////				throw new ElementNotFoundException();
////			}
////
////			if (eachManagerDetails.getFirstName().isBlank() || eachManagerDetails.getFirstName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (eachManagerDetails.getLastName().isBlank() || eachManagerDetails.getLastName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (eachManagerDetails.getUserName().isBlank() || eachManagerDetails.getUserName().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (eachManagerDetails.getPassword().isBlank() || eachManagerDetails.getPassword().isEmpty()) {
////				throw new InputUserException();
////			}
////			if (eachManagerDetails.getUserType().isBlank() || eachManagerDetails.getUserType().isEmpty()) {
////				throw new InputUserException();
////			}
////		}
////
////		for (ManagerDTO eachManagerDetails : managerDTOLists) {
////			Manager updateManagerDetails = managerRepo.findById(eachManagerDetails.getManagerId()).orElse(null);
////			updateManagerDetails.setManagerId(updateManagerDetails.getManagerId());
////			updateManagerDetails.setFirstName(updateManagerDetails.getFirstName().toUpperCase());
////			updateManagerDetails.setLastName(updateManagerDetails.getLastName().toUpperCase());
////			updateManagerDetails.setUserName(updateManagerDetails.getUserName());
////			updateManagerDetails.setPassword(updateManagerDetails.getPassword());
////			updateManagerDetails.setUserType(updateManagerDetails.getUserType().toUpperCase());
////
////			// ------------- add manager home address details --------------//
////			Address updateAddress = addressRepo.findById(eachManagerDetails.getAddressDTO().getAddressId())
////					.orElse(null);
////			updateAddress.setAddressId(eachManagerDetails.getAddressDTO().getAddressId());
////			updateAddress.setHouseNumber(eachManagerDetails.getAddressDTO().getHouseNumber());
////			updateAddress.setHouseName(eachManagerDetails.getAddressDTO().getHouseName().toUpperCase());
////			updateAddress.setStreetName(eachManagerDetails.getAddressDTO().getStreetName().toUpperCase());
////			updateAddress.setCityName(eachManagerDetails.getAddressDTO().getCityName().toUpperCase());
////			updateManagerDetails.setAddress(updateAddress);
////
////			// ----------- assing branch to the manager -----------//
////			Branch updateBranch = branchRepo.findById(eachManagerDetails.getBranchId()).orElse(null);
////			updateBranch.setBranchId(eachManagerDetails.getBranchId());
////			updateManagerDetails.setBranchId(updateBranch.getBranchId());
////
////			managerRepo.save(updateManagerDetails);
////			updateListsofManagerDetails.add(updateManagerDetails);
////		}
////
////		return updateListsofManagerDetails;
//		return null;
//	}
//
//	@Override
//	public boolean findManagerById(long managerId) {
//		Manager existingManger = managerRepo.findById(managerId).orElse(null);
//		if (existingManger == null) {
//			log.info("Entered manager id " + managerId + " not present in system");
//			return false;
//		}
//		log.info("Entered manager id " + managerId + " present in system");
//		return true;
//	}
//
//	@Override
//	public Manager findByFirstNameAndLastName(String firstName, String lastName) {
////		Manager existingManager = managerRepo.findByFirstNameAndLastName(firstName, lastName);
////		if (existingManager == null) {
////			throw new ElementNotFoundException();
////		}
////		return existingManager;
//		return null;
//	}
//
//	@Override
//	public List<Manager> fetchAllManagerListsInAlphabeticalOrder() {
//		List<Manager> existingManagerLists = managerRepo.findAll();
//		if (existingManagerLists.isEmpty()) {
//			throw new ElementNotFoundException();
//		}
//		List<Manager> alphabeticalOrderManagerLists = existingManagerLists.stream()
//				.sorted((e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName())).collect(Collectors.toList());
//
//		return alphabeticalOrderManagerLists;
//	}
//
//	@Override
//	public Manager findManagerByBranchId(long branchId) {
////		Manager existingManager = managerRepo.findByBranchId(branchId);
////		if (existingManager == null) {
////			throw new ElementNotFoundException();
////		}
////		return existingManager;
//		return null;
//	}
//
//	@Override
//	public void deleteManager(long managerId) {
//		Manager existingManager = managerRepo.findById(managerId).orElse(null);
//		if (existingManager == null) {
//			throw new ElementNotFoundException();
//		}
//		managerRepo.deleteById(managerId);
//	}
//

}
