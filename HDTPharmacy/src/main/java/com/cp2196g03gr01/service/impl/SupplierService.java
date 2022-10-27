package com.cp2196g03gr01.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Supplier;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.repository.ISupplierRepository;
import com.cp2196g03gr01.service.ISupplierService;

@Service
public class SupplierService implements ISupplierService {

	@Autowired
	private ISupplierRepository supplierRepository;

	@Override
	public void createSupplier(Supplier supplier) {
		supplierRepository.save(supplier);
	}

	@Override
	public Page<Supplier> showAllSupplier(String keyword, Pageable pageable) {

		if (keyword.trim().isEmpty())
			return supplierRepository.findAll(pageable);

		return supplierRepository.findByNameContains(keyword, pageable);
	}

	@Override
	public Supplier findById(Long id) {
		return supplierRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Supplier not found"));
	}

	@Override
	public Supplier save(@Valid Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public String delete(Long id) {
		String message = "";
//		boolean hasChildren = categoryRepository.findByParentCategoryId(id).size() > 0;
//		if (!hasChildren)
//			categoryRepository.delete(categoryRepository.findById(id)
//					.orElseThrow(() -> new NoSuchElementException("Category not found")));
//		else {
//			message = "Phân loại có phân loại khác tham chiếu không thể xóa";
//		}
		Optional<Supplier> supplier = supplierRepository.findById(id);
		if (supplier.isPresent())
			supplierRepository.delete(supplier.get());
		return message;

	}

}
