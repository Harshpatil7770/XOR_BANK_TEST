package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

	private long addressId;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;
}
