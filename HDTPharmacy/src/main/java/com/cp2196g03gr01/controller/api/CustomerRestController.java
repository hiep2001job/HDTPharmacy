package com.cp2196g03gr01.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.service.ICustomerService;
import com.cp2196g03gr01.service.impl.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {
	
	@Autowired
	private ICustomerService customerService;
	
	@PostMapping()
	public Customer createCustomer(@RequestBody Customer customer){
		Customer result=customerService.save(customer);
		return result;
	}
	
}
