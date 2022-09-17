package com.xoriant.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactinId;

	private double creaditAmount;

	private double debitAmount;

	private double accountBalance;
}