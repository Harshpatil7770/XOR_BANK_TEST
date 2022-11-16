package com.xoriant.bank.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomValue {

	public static final String VALUE = "9000";

	public long getRandomValue() {
		Random random = new Random();
		return random.nextLong(9000);
	}
}
