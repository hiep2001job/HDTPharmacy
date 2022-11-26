package com.cp2196g03gr01.exception;

public class ProductOutOfStockException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String getMessage() {
		return "Số lượng sản phẩm trong kho không đủ";
	}

}
