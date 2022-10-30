package com.cp2196g03gr01.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cp2196g03gr01.entity.RetailInvoice;

public interface IRetailService {
	RetailInvoice findById(Long id);

	RetailInvoice save(RetailInvoice invoice);
	
	Page<RetailInvoice> showAllRetailInvoice(String term, Pageable pageable);
}