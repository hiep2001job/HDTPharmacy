package com.cp2196g03gr01.entity;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = {@Index(name = "fulltextIndex",columnList = "product_name")})
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_name", length = 200)
	private String name;

	@Column(name = "product_barcode", length = 50)
	private String barcode;

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

	@Column(name = "product_packing", length = 100)
	private String packing;

	@Column(name = "product_img_primary")
	private String imagePrimary;

	@Column(name = "product_img_1")
	private String image1;

	@Column(name = "product_img_2")
	private String image2;

	@Column(name = "product_img_3")
	private String image3;

	@Column(name = "product_img_4")
	private String image4;
	
	@Column(name="product_alias", length=200, nullable=false, unique=true)
    private String alias;

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
	
	@ManyToOne(targetEntity = Category.class, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private Category category;

//	Information of drug end

	@Transient
	public List<String> getPhotoPathList() {
		List<String> list = new ArrayList<String>();
		if (imagePrimary != null)
			list.add("/product-images/" + this.imagePrimary);
		if (image1 != null)
			list.add("/product-images/" + this.image1);
		if (image2 != null)
			list.add("/product-images/" + this.image2);
		if (image3 != null)
			list.add("/product-images/" + this.image3);
		if (image4 != null)
			list.add("/product-images/" + this.image4);
		return list;
	}

	@Transient
	public String getPrimaryPhotoPath() {
		String path="";
		if(this.imagePrimary!=null)
			path="/product-images/" + this.imagePrimary;
		return path;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", barcode=" + barcode + ", stock=" + stock + ", position="
				+ position + ", salePrice=" + salePrice + ", weight=" + weight + ", rewardPoint=" + rewardPoint
				+ ", packing=" + packing + ", imagePrimary=" + imagePrimary + ", image1=" + image1 + ", image2="
				+ image2 + ", image3=" + image3 + ", image4=" + image4 + ", isDrug=" + isDrug + ", drugId=" + drugId
				+ ", initialName=" + initialName + ", drugGroup=" + drugGroup + ", absorbingWay=" + absorbingWay
				+ ", regisNumber=" + regisNumber + ", drugGeneric=" + drugGeneric + ", concentration=" + concentration
				+ ", manufacturer=" + manufacturer + ", medicineSubstance=" + medicineSubstance + ", medicinalHerbs="
				+ medicinalHerbs + ", isOnlineSale=" + isOnlineSale + ", unitPrices=" + unitPrices + ", category="
				+ category + "]";
	}
	
}
