package com.cp2196g03gr01.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.repository.ICustomerRepository;
import com.cp2196g03gr01.service.ICustomerService;

@Service
public class CustomerService implements ICustomerService {
	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public void createCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public Page<Customer> showAllCustomer(String keyword, Pageable pageable) {
		
		if(keyword.trim().isEmpty())
			return customerRepository.findAll(pageable);
		
		return customerRepository.findByNameContains(keyword,pageable);
	}

	@Override
	public List<Customer> showAllCustomer() {
		return customerRepository.findAll();
	}

	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Customer not found"));
	}

	@Override
	public Customer save(@Valid Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public String delete(Long id) {
		String message = "";
		Optional<Customer> c = customerRepository.findById(id);
		if (c.isPresent())
			customerRepository.delete(c.get());
//		boolean hasChildren = customerRepository.findByParentCustomerId(id).size() > 0;
//		if (!hasChildren)
//			customerRepository.delete(customerRepository.findById(id)
//					.orElseThrow(() -> new NoSuchElementException("Customer not found")));
//		else {
//			message = "Khách hàng có phân loại khác tham chiếu không thể xóa";
//		}
		return message;

	}
}
