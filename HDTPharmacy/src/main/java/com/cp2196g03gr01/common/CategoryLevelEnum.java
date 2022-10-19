package com.cp2196g03gr01.common;

public enum CategoryLevelEnum {
	MENU("Menu chính"), SUBMENU("Menu con"), LIST("Danh sách");

	private final String text;

	/**
	 * @param text
	 */
	CategoryLevelEnum(final String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	};
}
