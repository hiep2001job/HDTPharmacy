package com.cp2196g03gr01.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class DrugPriceWithUnit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Product.class,cascade = CascadeType.ALL)
	private Product product;
	
	@Column( length = 200)
	private String unitName;

	private Long unitPrice;
	
	private Boolean isPrimary;
}
