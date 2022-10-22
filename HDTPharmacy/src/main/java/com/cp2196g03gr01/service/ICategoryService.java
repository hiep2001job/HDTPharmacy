package com.cp2196g03gr01.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cp2196g03gr01.entity.Category;

public interface ICategoryService {
	void createCategory(Category category);

	Page<Category> showAllCategory(String keyword, Pageable pageable);

	Category findById(Long id);

	Category save(@Valid Category category);

	List<Category> showAllCategory();

	String delete(Long id);

}
