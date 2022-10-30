package com.cp2196g03gr01.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.RetailInvoice;
import com.cp2196g03gr01.repository.IRetailRepository;
import com.cp2196g03gr01.service.IRetailService;

@Service
public class RetailInvoiceService implements IRetailService {
	@Autowired
	private IRetailRepository retailRepository;

	@Override
	public RetailInvoice findById(Long id) {
		return retailRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Retail invoice not found"));
	}

	@Override
	public RetailInvoice save(RetailInvoice invoice) {
		return retailRepository.save(invoice);
	}
	@Override
	public Page<RetailInvoice> showAllRetailInvoice(String term, Pageable pageable) {
		return retailRepository.findAllByOrderByIdDesc(pageable);
	}
}
