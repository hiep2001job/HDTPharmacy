package com.cp2196g03gr01.common;

public enum AzureBlobContainerEnum {
	CATEGORY_IMAGE("category-images"),
	PRODUCT_IMAGE("product-images"),
	USER_IMAGE("user-images"),
	SUPPLIER_IMAGE("supplier-images");

	private final String text;

	/**
	 * @param text
	 */
	AzureBlobContainerEnum(final String text) {
		this.text = text;
	}

	public String getValue() {
		return this.text;
	};
}
