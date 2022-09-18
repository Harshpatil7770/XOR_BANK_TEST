package com.xoriant.bank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "login_details")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_id")
	private long loginId;

	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;

}
