package com.cp2196g03gr01.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cp2196g03gr01.entity.Product;

public interface IProductService {
	Page<Product> showAllProduct(String keyword, Pageable pageable);

	Product findById(Long id);

	Product save(@Valid Product category);


	void delete(Long id);
}
