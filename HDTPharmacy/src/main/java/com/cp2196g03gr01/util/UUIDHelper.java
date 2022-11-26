package com.cp2196g03gr01.util;

import java.util.Random;

public class UUIDHelper {

	public static String makeUUID() {
		return String.format("%04d", new Random().nextInt(10000)).toString();
	}
}
