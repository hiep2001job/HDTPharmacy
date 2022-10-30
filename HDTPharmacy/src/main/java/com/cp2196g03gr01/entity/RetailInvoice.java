package com.cp2196g03gr01.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "retail_invoice")
public class RetailInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private String note;

	@Fetch(FetchMode.JOIN)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = true)
	private Customer customer;
	
	@Fetch(FetchMode.JOIN)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	private Date createAt;

	@Fetch(FetchMode.JOIN)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "retail_invoice_id")
	private List<RetailInvoiceDetail> items;

	@XmlTransient
	public Customer getCustomer() {
		return customer;
	}

	private Long total;

	@PrePersist
	public void prePersist() {
		createAt = new Date();
		total = getTotal();
	}

	public RetailInvoice() {
		super();
		this.items = new ArrayList<RetailInvoiceDetail>();
	}

	@Transient
	public Integer getAmount() {
		int amount = 0;
		for (RetailInvoiceDetail detail : items) {
			amount += detail.getAmount().intValue();
		}
		return amount;
	}

	public Long getTotal() {
		Long total = 0L;
		int size = items.size();

		for (int i = 0; i < size; i++) {
			total += items.get(i).calculateImport();
		}

		return total;
	}

	public void addItemInvoice(RetailInvoiceDetail item) {
		this.items.add(item);
	}

}
