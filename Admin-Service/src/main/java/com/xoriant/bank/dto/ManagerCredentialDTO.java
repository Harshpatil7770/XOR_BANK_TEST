package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCredentialDTO {

	private int id;

	private String userName;

	private String password;

}
