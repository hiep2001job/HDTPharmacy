package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cp2196g03gr01.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
	
	Category findByAlias(String alias);

	List<Category> findByParentCategoryIsNull();

	Optional<Category> findById(Long id);	
	
	List<Category> findByParentCategoryId(Long id);
	
	@Query(value = "SELECT cat.* FROM category cat WHERE MATCH(cat.category_name) AGAINST (:name IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    public Page<Category> findFullTextSearchByName(@Param("name") String name,Pageable pageable);
}
