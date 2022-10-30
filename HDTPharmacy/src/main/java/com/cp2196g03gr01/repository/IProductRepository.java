package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cp2196g03gr01.dto.ProductDTO;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.projection.IProductProjection;

public interface IProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findById(Long id);

	@Query(value = "SELECT pro.* FROM product pro WHERE MATCH (pro.product_name) "
			+ "AGAINST (:name IN BOOLEAN MODE) OR pro.product_name LIKE %:name%", nativeQuery = true)
	public Page<Product> findFullTextSearchByName(@Param("name") String name, Pageable pageable);

//	@Query(value = "SELECT pro.id, pro.product_name, pro.product_sale_price,pro.product_img_primary,pro.product_packing FROM product pro WHERE MATCH (pro.product_name) AGAINST (:name IN BOOLEAN MODE) OR pro.product_name LIKE %:name%"
//			, nativeQuery = true)
//	public Page<ProductDTO> findFulltextForSuggestion(@Param("name") String name, Pageable pageable);

	@Query(value = "SELECT pro.id ,pro.product_name as name"
			+ ",pro.product_sale_price as price,pro.product_img_primary as image,pro.product_packing as packaging "
			+ "FROM product pro " + "WHERE MATCH (pro.product_name) "
			+ "AGAINST (:name IN BOOLEAN MODE) OR pro.product_name LIKE %:name%", nativeQuery = true)
	public Page<IProductProjection> findFulltextForSuggestion(@Param("name") String name, Pageable pageable);
}
