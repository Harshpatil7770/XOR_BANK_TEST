package com.xoriant.bank.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_creaditional")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCredential implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String userName;

	private String password;

}
