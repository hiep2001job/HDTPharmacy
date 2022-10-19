package com.cp2196g03gr01.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_name", length = 200)
	private String name;

	@Column(name = "product_barcode", length = 50)
	private String barcode;
	
	@Column(name = "product_commisstion")
	private Float employeeCommission;
	
	@Column(name = "product_stock")
	private Integer stock;
	
	@Column(name = "product_position", length = 150)
	private String position;
	
	@Column(name = "product_sale_price")
	private Long salePrice;
	
	@Column(name = "product_weight")
	private Float weight;
	
	@Column(name = "product_reward_point")
	private Integer rewardPoint;
	
	@Column(name = "product_packing",length = 100)
	private String packing;
	
	@OneToMany(mappedBy = "product")
	private List<ProductImage> images;

//	Information of drug start

	@Column(name = "product_is_drug")
	private Boolean isDrug;

	@Column(name = "product_drug_id", length = 200)
	private String drugId;

	@Column(name = "product_drug_initialname", length = 100)
	private String initialName;

	@Column(name = "product_drug_group", length = 100)
	private String drugGroup;

	@Column(name = "product_absorbingway", length = 100)
	private String absorbingWay;
	
	@Column(name = "product_regis_number", length = 100)
	private String regisNumber;
	
	@Column(name = "product_drug_generic", length = 200)
	private String drugGeneric;
	
	@Column(name = "product_drug_concentration", length = 100)
	private String concentration;
	
	@Column(name = "product_manufacturer", length = 200)
	private String manufacturer;
	
	@Column(name = "product_drug_medicine_substance", length = 200)
	private String medicineSubstance;
	
	@Column(name = "product_drug_medicinal_herbs", length = 200)
	private String medicinalHerbs;
	
	@Column(name = "product_online_sale", length = 200)
	private Boolean isOnlineSale;
	
	@OneToMany(mappedBy = "product")
	private List<DrugPriceWithUnit> unitPrices;
	
	
//	Information of drug end

}
