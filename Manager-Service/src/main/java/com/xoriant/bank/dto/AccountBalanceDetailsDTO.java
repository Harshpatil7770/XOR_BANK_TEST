package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDetailsDTO {

	private long transactinId;

	private double creaditAmount;

	private double debitAmount;

	private double accountBalance;
}
