package com.xoriant.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

	private long addressId;

	private long houseNumber;

	private String houseName;

	private String streetName;

	private String cityName;

}
