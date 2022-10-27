package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cp2196g03gr01.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findById(Long id);	
	
	@Query(value = "SELECT cus.* FROM customer cus WHERE MATCH(cus.customer_name) AGAINST (:name IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    public Page<Customer> findFullTextSearchByName(@Param("name") String name,Pageable pageable);
	
	Page<Customer> findByNameContains(String name,Pageable pageable);
}
