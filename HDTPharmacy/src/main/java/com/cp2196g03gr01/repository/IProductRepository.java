package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Product;

public interface IProductRepository  extends JpaRepository<Product, Long>{


	Optional<Product> findById(Long id);	
	
	
	@Query(value = "SELECT pro.* FROM product pro WHERE MATCH (pro.product_name) AGAINST (:name IN BOOLEAN MODE)", nativeQuery = true)
    public Page<Product> findFullTextSearchByName(@Param("name") String name,Pageable pageable);
}
