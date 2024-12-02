package com.swyp.mema.global.utils;

import java.security.SecureRandom;

public class RandomCodeGenerator {

	private static final int MIN = 100000; // 6자리 최소값
	private static final int MAX = 999999; // 6자리 최대값

	private static final SecureRandom random = new SecureRandom();

	public static int generateCode() {
		return random.nextInt(MAX - MIN + 1) + MIN; // 100000 ~ 999999
	}
}
