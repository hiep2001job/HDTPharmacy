package com.cp2196g03gr01.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.cp2196g03gr01.entity.Supplier;

public interface ISupplierService {
	void createSupplier(Supplier supplier);

	Page<Supplier> showAllSupplier(String keyword, Pageable pageable);

	Supplier findById(Long id);

	Supplier save(@Valid Supplier supplier);

	String delete(Long id);

}
