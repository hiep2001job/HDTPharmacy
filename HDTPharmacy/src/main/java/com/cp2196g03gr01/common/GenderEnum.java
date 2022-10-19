package com.cp2196g03gr01.common;

public enum GenderEnum {
	MALE("Nam"), FEMALE("Nữ");

	private final String text;

	/**
	 * @param text
	 */
	GenderEnum(final String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	};
}
