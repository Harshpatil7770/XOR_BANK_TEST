package com.xoriant.bank.constant;

import java.util.Random;

public class LoginConstantDetails {

	private LoginConstantDetails() {

	}

	public static final String VALUE = "10000";

	public static long getAccountNumber() {
		Random random = new Random();
		Long uniqueAccountNumber = Long.valueOf(random.nextInt(9000) + VALUE);
		return uniqueAccountNumber;
	}
}
