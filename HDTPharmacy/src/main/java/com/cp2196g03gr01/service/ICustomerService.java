package com.cp2196g03gr01.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cp2196g03gr01.entity.Customer;

public interface ICustomerService {
	void createCustomer(Customer customer);

	Page<Customer> showAllCustomer(String keyword, Pageable pageable);

	Customer findById(Long id);

	Customer save(@Valid Customer customer);

	List<Customer> showAllCustomer();

	String delete(Long id);
}
