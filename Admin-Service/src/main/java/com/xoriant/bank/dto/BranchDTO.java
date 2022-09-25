package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

	private long branchId;

	private String branchName;

	private String ifscCode;

	private AddressDTO addressDTO;
}
