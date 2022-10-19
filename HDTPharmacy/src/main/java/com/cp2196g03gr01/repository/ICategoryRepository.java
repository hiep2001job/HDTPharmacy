package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp2196g03gr01.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
	
	Category findByAlias(String alias);

	List<Category> findByParentCategoryIsNull();

	Optional<Category> findById(Long id);	
	
	List<Category> findByParentCategoryId(Long id);
}
