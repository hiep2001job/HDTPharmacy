package com.cp2196g03gr01.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.repository.IProductRepository;
import com.cp2196g03gr01.service.IProductService;

@Service
public class ProductService implements IProductService{
	@Autowired
	private IProductRepository productRepository;

	@Override
	public Page<Product> showAllProduct(String keyword, Pageable pageable) {
		
		if(keyword.trim().isEmpty())
			return productRepository.findAll(pageable);
		return productRepository.findFullTextSearchByName(keyword,pageable);
	}

	@Override
	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
		
	}

	@Override
	public Product save(@Valid Product product) {
		return productRepository.save(product);
	}

	

	@Override
	public String delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
