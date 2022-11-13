package com.xoriant.bank.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.bank.dto.ManagerDTO;
import com.xoriant.bank.exception.ElementNotFoundException;
import com.xoriant.bank.exception.InputUserException;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.model.ErrorCode;
import com.xoriant.bank.model.Manager;
import com.xoriant.bank.model.ManagerCredential;
import com.xoriant.bank.repo.BranchRepo;
import com.xoriant.bank.repo.ManagerRepo;
import com.xoriant.bank.util.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Service
public class ManagerServiceImpl implements ManagerService {

	private final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

	@Autowired
	private ManagerServiceImplDetails managerServiceImplDetails;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private RuntimeManager runtimeManager;

	@Value("${XOR_RETRY_COUNT}")
	private int XOR_RETRY_COUNT;

	@Value("${XOR_WAIT_TIME}")
	private long XOR_WAIT_TIME;

	@Override
	public Manager addNewManager(ManagerDTO managerDTO) {
		Manager newManagerDetails = addNewManagerDetails(managerDTO, 0);
		return newManagerDetails;
	}

	private Manager addNewManagerDetails(ManagerDTO managerDTO, int retryCount) {
		Manager managerDetails = null;
		try {
			if (managerDTO != null) {
				try {
					managerDetails = managerServiceImplDetails.addNewManager(managerDTO);
				} catch (Exception e) {
					logger.error("ManagerServiceImpl - Exception occured while adding new Manger details ", e);
					throw new RuntimeException();
				}
			} else {
				logger.info("ManagerDTO is null. No need to proceed further.");
				return managerDetails;
			}
		} catch (Exception e) {
			logger.error("ManagerResource - Exception occured while adding new manager details ", e);
			if (retryCount < XOR_RETRY_COUNT) {
				retryCount++;
				logger.info("ManagerResource - Retrying routing of message count {} ", retryCount);
				addNewManagerDetails(managerDTO, retryCount);
			} else {
				logger.info("ManagerResource - Maximum message routing retry count is reached {} ", retryCount);
				runtimeManager.getErrorController(ErrorCode.NEW_MANAGER_ADDITION_FAILED);
			}

		}
		return managerDetails;
	}

	@Override
	public Manager updateManagerDetails(ManagerDTO managerDTO) {
		Manager updatedManagerDetails = updateExistingManagerDetails(managerDTO, 0);
		return updatedManagerDetails;
	}

	private Manager updateExistingManagerDetails(ManagerDTO managerDTO, int retryCount) {
		try {
			Manager updateManager = null;
			if (managerDTO != null) {
				updateManager = managerServiceImplDetails.updateManagerDetails(managerDTO);
				return updateManager;
			}
		} catch (Exception e) {
			logger.error("ManagerResource - Exception occured updating manager details ", e);
			if (retryCount < XOR_RETRY_COUNT) {
				retryCount++;
				logger.info("ManagerResource - Retrying routing of message count {} ", retryCount);
				updateExistingManagerDetails(managerDTO, retryCount);
			} else {
				logger.info("ManagerResource - Maximun message routing retry count is reached {} ", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_UPDATING_THE_DETAILS);
			}
		}
		logger.info("ManagerDTO is null. No need to proceed further. ");
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
	@Cacheable(value = "admin-servicecache")
	public List<Manager> fetchAllManagerDetails() {
		List<Manager> existingManagerDetails = fetchAllPresentExistingManagerDetails(0);
		return existingManagerDetails;
	}

	private List<Manager> fetchAllPresentExistingManagerDetails(int retryCount) {
		List<Manager> existingManagerLists = null;
		try {
			existingManagerLists = managerRepo.findAllManagerWithTheirBranchDetails();
			if (existingManagerLists.isEmpty()) {
				logger.info("Manager details not found in system. No need to proceed further.");
				return null;
			}
		} catch (Exception e) {
			logger.error("ManagerResource- Exception occured while fetching data ", e);
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					logger.info("ManagerResource- Retrying routing of message count {} ", retryCount);
					fetchAllPresentExistingManagerDetails(retryCount);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} else {
				logger.info("ManagerResource- Maximum message routing count is reached {} ", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		logger.info("Manager details found in system");
		return existingManagerLists;

	}

	@Override
	@Cacheable(value = "admin-servicecache", key = "#managerId")
	public Manager findManagerById(long managerId) {
		Manager existingManagerDetails = findExistingManagerById(managerId, 0);
		return existingManagerDetails;
	}

	private Manager findExistingManagerById(long managerId, int retryCount) {
		Manager existingManager = null;
		try {
			existingManager = managerRepo.findById(managerId).orElse(null);
			if (existingManager == null) {
				logger.info("Manager details not found in system.");
				return null;
			}
		} catch (Exception e) {
			logger.error("ManagerResource - Exception occured while fetching manager details using id", e);
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					logger.info("ManagerResource- Retrying routing of message count {} ", retryCount);
					findExistingManagerById(managerId, retryCount);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} else {
				logger.info("ManagerResource- Maximun message routing retry is reached {} ", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		logger.info("Manager details found in system.");
		return existingManager;
	}

	@Override
	public Manager findByFirstNameAndLastName(String firstName, String lastName) {

		Manager managerInfo = fetchManagerDetailsBasedOnFirstAndLastName(firstName, lastName, 0);
		return managerInfo;
	}

	private Manager fetchManagerDetailsBasedOnFirstAndLastName(String firstName, String lastName, int retryCount) {
		Manager existingManager = null;
		try {
			existingManager = managerRepo.findByFirstNameAndLastName(firstName, lastName);
			if (existingManager == null) {
				logger.info("Entered manager details not found system.");
				return null;
			}
		} catch (Exception e) {
			logger.error("ManagerServiceImpl - Exception occured while fetching data.", e);
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					logger.info("ManagerServiceImpl - Retrying routing of message count {} ", retryCount);
					fetchManagerDetailsBasedOnFirstAndLastName(firstName, lastName, retryCount);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("ManagerServiceImpl - Maximun retrying rotuing of message is reached {} ", retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		logger.info("Entered manager details found system.");
		return existingManager;
	}

	@Override
	public List<Manager> fetchAllManagerListsInAlphabeticalOrder() {
		List<Manager> existingManagerLists = fetchExistingManagerListsOnAlphabeticalOrderOfThierNames(0);
		return existingManagerLists;
	}

	private List<Manager> fetchExistingManagerListsOnAlphabeticalOrderOfThierNames(int retryCount) {
		List<Manager> alphabeticalOrderManLists = null;
		try {
			List<Manager> existingManagerLists = managerRepo.findAll();
			if (existingManagerLists == null) {
				logger.info("Manager details not present in system.");
				return null;
			}
			alphabeticalOrderManLists = existingManagerLists.stream()
					.sorted((e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName())).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("ManagerServiceImpl - Exception occured while fetching data.");
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					logger.info("ManagerServiceImpl - Retrying routing of message count {} ", retryCount);
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					fetchExistingManagerListsOnAlphabeticalOrderOfThierNames(retryCount);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("ManagerServiceImpl - Maximun Retrying routing of message count {} is reached ",
						retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		return alphabeticalOrderManLists;
	}

	@Override
	public Manager findManagerByBranchId(long branchId) {
		Manager existingManagerDetails = findManagerDetailsByBranchId(branchId, 0);
		return existingManagerDetails;
	}

	private Manager findManagerDetailsByBranchId(long branchId, int retryCount) {
		Manager existingManagerDetails = null;
		try {
			Branch existingBranch = branchRepo.findById(branchId).orElse(null);
			if (existingBranch == null) {
				logger.info("Entered branch id details not found in system.");
				return null;
			}
			existingManagerDetails = managerRepo.findByBranchId(branchId).orElse(null);
			if (existingManagerDetails == null) {
				logger.info("Entered branch id not assigned to any manager.");
				return null;
			}
			return existingManagerDetails;
		} catch (Exception e) {
			logger.error("ManagerServiceImpl - Exception occured while fetching data.");
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					logger.info("ManagerServiceImpl - Retrying routing of message count {} ", retryCount);
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					findManagerDetailsByBranchId(branchId, retryCount);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("ManagerServiceImpl - Maximun Retrying routing of message count {} is reached ",
						retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		return existingManagerDetails;
	}

	@Override
	@Transactional
	public boolean deleteManager(long managerId) {
		boolean deletedExistingRecord = deleteExistingManagerRecords(managerId, 0);
		return deletedExistingRecord;
	}

	private boolean deleteExistingManagerRecords(long managerId, int retryCount) {
		try {
			Manager existingManger = managerRepo.findById(managerId).orElse(null);
			if (existingManger == null) {
				logger.info("Entered manager id details not present in system.");
				return false;
			}
			managerRepo.deleteById(managerId);
			logger.info("Deleted manager details succesfully");
		} catch (Exception e) {
			logger.error("ManagerServiceImpl - Exception occured while fetching data.");
			if (retryCount < XOR_RETRY_COUNT) {
				try {
					retryCount++;
					logger.info("ManagerServiceImpl - Retrying routing of message count {} ", retryCount);
					Thread.sleep(retryCount * XOR_WAIT_TIME);
					deleteExistingManagerRecords(managerId, retryCount);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("ManagerServiceImpl - Maximun Retrying routing of message count {} is reached ",
						retryCount);
				runtimeManager.getErrorController(ErrorCode.FAILED_FETCHING_MANAGER_DETAILS);
			}
		}
		return true;
	}

}
