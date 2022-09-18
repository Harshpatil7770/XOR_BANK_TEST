package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

	private long id;

	private String firstName;

	private String lastName;

	private String gender;

	private long adharNumber;

	private long mobileNumber;

	private String dateOfBirth;

	private AddressDTO addressDTO;

	private AccountDetailsDTO accountDetailsDTO;

	private LoginDetailsDTO loginDetailsDTO;
}
