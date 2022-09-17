package com.xoriant.bank.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_details")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "First Name is manadotory !")
	private String firstName;

	private String lastName;

	@Pattern(regexp = "^[a-zA-Z ]*$")
	private String gender;

//	@Pattern(regexp = "^[0-9]{12}$", message = "Adhar Number is mandatory !")
	@NotNull
	private long adharNumber;

	// @Pattern(regexp = "^[0-9]{10}$")
	private long mobileNumber;

	private Date dateOfBirth;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	private AccountDetails accountDetails;

}
