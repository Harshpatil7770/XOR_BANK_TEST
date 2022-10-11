package com.xoriant.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xoriant.bank.dto.AddressDTO;
import com.xoriant.bank.dto.BranchDTO;
import com.xoriant.bank.model.Address;
import com.xoriant.bank.model.Branch;
import com.xoriant.bank.repo.BranchRepo;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

	@Mock
	private BranchRepo branchRepo;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	private Branch branch;

	private Address address;

	private BranchDTO branchDTO;

	private AddressDTO addressDTO;

	@Before
	public void setUp() {
		branchDTO = new BranchDTO();
		branchDTO.setBranchId(1);
		branchDTO.setBranchName("SBI Narkhed");
		branchDTO.setIfscCode("MAHB1878");
		addressDTO.setAddressId(101);
		addressDTO.setHouseNumber("JK Building");
		addressDTO.setStreetName("Mohol highway");
		addressDTO.setCityName("Narkhed");
		branchDTO.setAddressDTO(addressDTO);

		branch = new Branch();
		branch.setBranchId(branchDTO.getBranchId());
		branch.setBranchName(branchDTO.getBranchName());
		branch.setIfscCode(branchDTO.getIfscCode());
		address.setAddressId(branchDTO.getAddressDTO().getAddressId());
		address.setHouseNumber(branchDTO.getAddressDTO().getHouseNumber());
		address.setStreetName(branchDTO.getAddressDTO().getStreetName());
		address.setHouseName(branchDTO.getAddressDTO().getHouseName());
		address.setCityName(branchDTO.getAddressDTO().getCityName());
		branch.setAddress(address);
	}

	@Test
	public void addNewBranch() {
		when(branchRepo.save(branch)).thenReturn(branch);
		assertEquals(branch, adminServiceImpl.addNewBranch(branchDTO));
	}
}
