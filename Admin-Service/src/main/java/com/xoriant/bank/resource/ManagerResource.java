package com.xoriant.bank.resource;

//import java.util.List;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.xoriant.bank.dto.ManagerDTO;
//import com.xoriant.bank.model.Manager;
//import com.xoriant.bank.sender.AdminMsgSender;
//import com.xoriant.bank.service.BranchService;
//
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/manager")
//@Slf4j
//public class ManagerResource {
//
//	@Autowired
//	BranchService adminService;
//
////	@Autowired
////	AdminMsgSender adminMsgSender;
//
////	@PostMapping("/save")
////	public ResponseEntity<Manager> addNewManager(@Valid @RequestBody ManagerDTO managerDTO) {
////		log.info("addNewManager() called");
////		Manager response = adminService.addNewManager(managerDTO);
////		if (response != null) {
////			adminMsgSender.addNewManager("New Manager hired ManagerId : " + response.getManagerId()
////					+ " Manager First and Last Name is :   " + response.getFirstName() + response.getLastName()
////					+ " And Assinged BranchId is : " + response.getBranchId());
////		}
////		log.info("New Manager Added Succesfully also assinged  him/her branch.");
////		return new ResponseEntity<>(response, HttpStatus.CREATED);
////	}
////
////	@GetMapping("/findAll")
////	public ResponseEntity<List<Manager>> fetchAllManagerDetails() {
////		log.info("fetchAllManagerDetails() called");
////		List<Manager> response = adminService.fetchAllManagerDetails();
////		return new ResponseEntity<>(response, HttpStatus.OK);
////	}
//
////	@PutMapping("/update")
////	public ResponseEntity<Manager> updateManagerDetails(@Valid @RequestBody ManagerDTO managerDTO) {
////		log.info("updateManagerDetails() called");
////		Manager result = adminService.updateManagerDetails(managerDTO);
////		return new ResponseEntity<Manager>(result, HttpStatus.CREATED);
////	}
//}
