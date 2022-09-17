package com.xoriant.bank.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_details_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {

	@Id
	private long accountNumber;

	private int accountType;

	private Date accountOpeningDate;

	@OneToOne(cascade = CascadeType.ALL)
	private AccountBalanceDetails accountBalanceDetails;
}
