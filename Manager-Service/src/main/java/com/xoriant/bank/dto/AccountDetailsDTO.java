package com.xoriant.bank.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsDTO {

	private String response;

	private long accountNumber;

	private int accountType;

	private Date accountOpeningDate;

	private AccountBalanceDetailsDTO accountBalanceDetailsDTO;
}
