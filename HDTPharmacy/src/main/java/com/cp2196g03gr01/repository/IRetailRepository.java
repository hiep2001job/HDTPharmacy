package com.cp2196g03gr01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cp2196g03gr01.entity.RetailInvoice;

public interface IRetailRepository  extends JpaRepository<RetailInvoice, Long>{

	Page<RetailInvoice> findAllByOrderByIdDesc(Pageable pageable);
	
}
