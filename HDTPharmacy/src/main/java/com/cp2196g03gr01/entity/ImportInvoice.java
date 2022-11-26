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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "import_invoice")
public class ImportInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note = "";

	private Boolean isDeleted = false;

	private String deleteReason = "";

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id", nullable = true)
	private Supplier supplier;

	/* Person responsible for the invoice */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	private Date createAt;

	@Fetch(FetchMode.JOIN)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "import_invoice_id")
	private List<ImportInvoiceDetail> items;

	@XmlTransient
	public Supplier getSupplier() {
		return supplier;
	}

	private Long total;

	@PrePersist
	public void prePersist() {
		createAt = new Date();
		total = getTotal();
	}

	public ImportInvoice() {
		super();
		this.items = new ArrayList<ImportInvoiceDetail>();
	}

	@Transient
	public Integer getAmount() {
		int amount = 0;
		for (ImportInvoiceDetail detail : items) {
			amount += detail.getAmount().intValue();
		}
		return amount;
	}

	@Transient
	public Long getRewardPoint() {

		return getTotal() / 10000;
	}

	public Long getTotal() {
		Long total = 0L;
		int size = items.size();

		for (int i = 0; i < size; i++) {
			total += items.get(i).calculateImport();
		}

		return total;
	}

	public void addItemInvoice(ImportInvoiceDetail item) {
		this.items.add(item);
	}
}
