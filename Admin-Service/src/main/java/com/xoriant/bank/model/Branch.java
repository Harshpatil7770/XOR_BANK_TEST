package com.xoriant.bank.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "branch_details")
public class Branch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long branchId;

	@Pattern(regexp = "^[a-zA-Z ]*$")
	@Size(min = 1, max = 30, message = "Please enter branch name")
	@NotNull(message = "branch name is mandatory")
	@NotBlank(message = "branch name is mandatory")
	private String branchName;

	// @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC Code is
	// mandatory")
	@NotBlank(message = "IFSC Code is mandatory")
	private String ifscCode;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id")
	private Address address;

}
