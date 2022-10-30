package com.cp2196g03gr01.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
	
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public void createCategory(Category category) {
		categoryRepository.save(category);
	}

	@Override
	public Page<Category> showAllCategory(String keyword, Pageable pageable) {
		
		if(keyword.trim().isEmpty())
			return categoryRepository.findAll(pageable);
		
		return categoryRepository.findFullTextSearchByName(keyword,pageable);
	}

	@Override
	public List<Category> showAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public Category findById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
	}

	@Override
	public Category save(@Valid Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public String delete(Long id) {
		String message = "";
		boolean hasChildren = categoryRepository.findByParentCategoryId(id).size() > 0;
		if (!hasChildren)
			categoryRepository.delete(categoryRepository.findById(id)
					.orElseThrow(() -> new NoSuchElementException("Category not found")));
		else {
			message = "Phân loại có phân loại khác tham chiếu không thể xóa";
		}
		return message;

	}

}
