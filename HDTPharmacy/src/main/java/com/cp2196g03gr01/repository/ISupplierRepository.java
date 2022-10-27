package com.cp2196g03gr01.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cp2196g03gr01.entity.Supplier;

public interface ISupplierRepository extends JpaRepository<Supplier, Long>{
	Page<Supplier> findByNameContains(String name,Pageable pageable);
	
}
