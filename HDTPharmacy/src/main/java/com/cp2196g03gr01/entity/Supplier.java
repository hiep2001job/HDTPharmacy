package com.cp2196g03gr01.entity;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Long id;

	@Column(name = "supplier_name", length = 50, nullable = false)
	private String name;

	@Column(name = "supplier_alias", length = 50, nullable = false, unique = true)
	private String alias;

	@Column(name = "supplier_email", length = 50, nullable = false, unique = true)
	private String email;

	@Column(name = "supplier_phone", length = 20, nullable = false)
	private String phone;

	@Column(name = "supplier_address", length = 100, nullable = false)
	private String address;

	@Column(name = "supplier_image")
	private String image;

	@Transient
	public String getPhotoPath() {
		return "/supplier-images/" + this.image;
	}

}
