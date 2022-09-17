package com.xoriant.bank.constant;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ManagerServiceConstant {

	private ManagerServiceConstant() {

	}

	public static final String NEW_CUSTOMER_ADDED = "New Customer Account Created Succesfully !";

	public static final String VALUE = "10000";

	public static long getAccountNumber() {
		Random random = new Random();
		Long uniqueAccountNumber = Long.valueOf(random.nextInt(9000) + VALUE);
		return uniqueAccountNumber;
	}

	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		return currentDate;
	}
}
