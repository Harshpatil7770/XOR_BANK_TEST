package com.xoriant.bank.resource;
//
//import java.util.List;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.xoriant.bank.dto.BranchDTO;
//import com.xoriant.bank.dto.ManagerDTO;
//import com.xoriant.bank.model.Branch;
//import com.xoriant.bank.model.Manager;
//import com.xoriant.bank.service.AdminService;
//import com.xoriant.bank.util.ApplicationConstant;
//import com.xoriant.bank.util.KafkaConstant;
//
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/login")
//@Slf4j
//public class UserResource {
//
//	@Autowired
//	private AdminService adminService;
//
//	@Autowired
//	private KafkaTemplate<String, Branch> kafkaTemplateForBranch;
//
//	@Autowired
//	private KafkaTemplate<String, Manager> kafkaTemplateForManager;
//
//	@Autowired
//	private KafkaTemplate<String, List<Branch>> kafkaTemplateForBrachLists;
//
//	@Autowired
//	private KafkaTemplate<String, List<Manager>> kafkaTemplateForManangerLists;
//
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
//	@Value("${spring.kafka.topic}")
//	private String topic;
//
//	@PostMapping("/save")
//	public ResponseEntity<Branch> addNewBranch(@Valid @RequestBody BranchDTO branchDTO) {
//		log.info(ApplicationConstant.ADD_NEW_BRANCH);
//		Branch result = adminService.addNewBranch(branchDTO);
//		if (result != null) {
//			kafkaTemplateForBranch.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//
//	@PutMapping("/update")
//	public ResponseEntity<Branch> updateBranchDetails(@RequestBody BranchDTO branchDTO) {
//		log.info(ApplicationConstant.UPDATE_BRANCH_DETAILS);
//		Branch result = adminService.updateBranchDetails(branchDTO);
//		if (result != null) {
//			kafkaTemplateForBranch.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/fetchAll-branch")
//	public ResponseEntity<List<Branch>> fetchAllBranch() {
//		log.info(ApplicationConstant.FETCH_ALL_BRANCH);
//		List<Branch> result = adminService.fetchAllBranch();
//		if (result != null) {
//			kafkaTemplateForBrachLists.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/find")
//	public ResponseEntity<Branch> findByBranchId(@RequestParam long branchId) {
//		log.info(ApplicationConstant.FIND_BY_BRANCH_ID);
//		Branch result = adminService.findByBranchId(branchId);
//		if (result != null) {
//			kafkaTemplateForBranch.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/find/{branchName}")
//	public ResponseEntity<Branch> findByBranchName(@PathVariable String branchName) {
//		log.info(ApplicationConstant.FIND_BY_BRANCH_NAME);
//		Branch result = adminService.findByName(branchName);
//		if (result != null) {
//			kafkaTemplateForBranch.send(branchName, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/branch/{branchId}")
//	public ResponseEntity<String> deleteBranch(@PathVariable long branchId) {
//		log.info(ApplicationConstant.DELETE_BRANCH);
//		adminService.deleteBranch(branchId);
//		kafkaTemplate.send(topic, KafkaConstant.DELETE_BRANCH);
//		System.out.println(KafkaConstant.MESSAGE_SENDER);
//		return new ResponseEntity<>(KafkaConstant.DELETE_BRANCH, HttpStatus.OK);
//	}
//
//	@PostMapping("/save/manager")
//	public ResponseEntity<Manager> addNewManager(@Valid @RequestBody ManagerDTO managerDTO) {
//		log.info(ApplicationConstant.ADD_NEW_MANAGER);
//		Manager result = adminService.addNewManager(managerDTO);
//		if (result != null) {
//			kafkaTemplateForManager.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/fetchAll-manager")
//	public ResponseEntity<List<Manager>> fetchAllManager() {
//		log.info(ApplicationConstant.FETCH_ALL_MANAGER);
//		List<Manager> result = adminService.fetchAllManagerDetails();
//		if (result != null) {
//			kafkaTemplateForManangerLists.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@PutMapping("/update-manager")
//	public ResponseEntity<Manager> updateManagerDetails(@Valid @RequestBody ManagerDTO managerDTO) {
//		log.info(ApplicationConstant.UPDATE_MANAGER);
//		Manager result = adminService.updateManagerDetails(managerDTO);
//		if (result != null) {
//			kafkaTemplateForManager.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<Manager>(result, HttpStatus.CREATED);
//	}
//
//	@PostMapping("/saveAll-manager")
//	public ResponseEntity<List<Manager>> addNewListsOfManager(@Valid @RequestBody List<ManagerDTO> managerDTOLists) {
//		log.info(ApplicationConstant.ADD_NEW_LISTS_MANAGER);
//		List<Manager> result = adminService.addNewListsOfManager(managerDTOLists);
//		if (result != null) {
//			kafkaTemplateForManangerLists.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//
//	@PutMapping("/updateAll-manager")
//	public ResponseEntity<List<Manager>> updateListsOfManager(@Valid @RequestBody List<ManagerDTO> managerDTOLists) {
//		log.info(ApplicationConstant.UPDATE_LISTS_OF_MANAGER);
//		List<Manager> result = adminService.updateListsOfManager(managerDTOLists);
//		if (result != null) {
//			kafkaTemplateForManangerLists.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/find-manager/{managerId}")
//	public ResponseEntity<Manager> findByManagerId(@PathVariable long managerId) {
//		log.info(ApplicationConstant.FIND_MANAGER_BY_ID);
//		Manager result = adminService.findByManagerId(managerId);
//		if (result != null) {
//			kafkaTemplateForManager.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/find-manager/{firstName}/{lastName}")
//	public ResponseEntity<Manager> findByFirstNameAndLastName(@PathVariable String firstName,
//			@PathVariable String lastName) {
//		log.info(ApplicationConstant.FIND_MANAGER_BY_FIRST_AND_LAST_NAME);
//		Manager result = adminService.findByFirstNameAndLastName(firstName.toUpperCase(), lastName.toUpperCase());
//		if (result != null) {
//			kafkaTemplateForManager.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/fetchAll-manager/alphabeticalOrder")
//	public ResponseEntity<List<Manager>> fetchAllManagerListsInAlphabeticalOrder() {
//		log.info(ApplicationConstant.FETCH_ALL_MANAFER_LISTS_IN_ALPHABETICAL_ORDER);
//		List<Manager> result = adminService.fetchAllManagerListsInAlphabeticalOrder();
//		if (result != null) {
//			kafkaTemplateForManangerLists.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/find/branch-manager/{branchId}")
//	public ResponseEntity<Manager> findManagerByBranchId(@PathVariable long branchId) {
//		log.info(ApplicationConstant.FIND_MANAGER_BY_BRANCH_ID);
//		Manager result = adminService.findManagerByBranchId(branchId);
//		if (result != null) {
//			kafkaTemplateForManager.send(topic, result);
//			System.out.println(KafkaConstant.MESSAGE_SENDER);
//		}
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/delete/{managerId}")
//	public ResponseEntity<String> deleteManager(@PathVariable long managerId) {
//		log.info(ApplicationConstant.DELETE_MANAGER_BY_MANAGER_ID);
//		adminService.deleteManager(managerId);
//		kafkaTemplate.send(topic, KafkaConstant.DELETE_MANAGER_RECORDS);
//		System.out.println(KafkaConstant.MESSAGE_SENDER);
//		return new ResponseEntity<>(KafkaConstant.DELETE_MANAGER_RECORDS, HttpStatus.OK);
//
//	}
//
//}
