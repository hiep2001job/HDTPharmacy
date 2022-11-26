package com.cp2196g03gr01.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "import_invoice_detail")
public class ImportInvoiceDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer amount;
	
	private Long unitPrice;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;

	
	public Long calculateImport() {
		return amount * product.getSalePrice();
	}
	
	private Long total;
	
	@PrePersist
	public void prePersist() {
		unitPrice=product.getSalePrice();
		total=calculateImport();
	}
}
