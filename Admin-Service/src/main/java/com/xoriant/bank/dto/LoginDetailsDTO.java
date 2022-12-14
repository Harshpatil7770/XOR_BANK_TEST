package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetailsDTO {

	private long loginId;
	
	private long accountNumber;

	private String userName;

	private String password;
}
