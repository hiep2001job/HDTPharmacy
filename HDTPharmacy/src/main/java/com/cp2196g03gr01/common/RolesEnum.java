package com.cp2196g03gr01.common;

public enum RolesEnum {
	MANAGER("Quản lý"), EMPLOYEE("Nhân viên"), CUSTOMER("Khách hàng");

	private final String text;

	/**
	 * @param text
	 */
	RolesEnum(final String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	};
}
