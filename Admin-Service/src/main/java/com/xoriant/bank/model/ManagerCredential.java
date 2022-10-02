package com.xoriant.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_account_basic_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCredential {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String userName;

	private String password;

}
