package com.cp2196g03gr01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.projection.ICustomerProjection;

public interface ICustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findById(Long id);	
	
	@Query(value = "SELECT cus.* FROM customer cus WHERE MATCH(cus.customer_name) AGAINST (:name IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    public Page<Customer> findFullTextSearchByName(@Param("name") String name,Pageable pageable);
	
	Page<Customer> findByNameContains(String name,Pageable pageable);
	
	@Query(value="SELECT c.customer_id as id, c.customer_name as name , c.customer_phone as phone "
			+ "FROM customer c WHERE (c.customer_name LIKE %:term%) OR (c.customer_phone  LIKE %:term%)"
			,nativeQuery=true)
	Page<ICustomerProjection> searchCustomers(@Param("term") String term,Pageable pageable);
}
