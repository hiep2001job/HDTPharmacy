package com.cp2196g03gr01.dto;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ProductDTO {

	private Long id;

	private String name;

	private String image;

	private Integer salePrice;

	private String packing;
}
