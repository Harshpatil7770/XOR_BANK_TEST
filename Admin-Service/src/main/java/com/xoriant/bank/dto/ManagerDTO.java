package com.xoriant.bank.dto;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {

	private long managerId;

	@Pattern(regexp = "^[A-Za-z ]*$")
	@Size(min = 1, max = 30, message = "Please enter your first name")
	@NotBlank
	private String firstName;

	@Pattern(regexp = "^[A-Za-z ]*$")
	@Size(min = 1, max = 30, message = "Please enter your last name")
	@NotBlank
	private String lastName;

//	@NotNull(message = "Please enter user name")
//	private String userName;
//
//	// ----------- Minimun 8 character, atleast 1 letter and 1 number ----------//
//	// @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
//	private String password;
	
	private ManagerCredentialDTO credentialDTO;

	@NotBlank(message = "Please enter user type")
	private String userType;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private AddressDTO addressDTO;

	private long branchId;
}
