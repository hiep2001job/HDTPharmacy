package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.util.Random;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.service.ICustomerService;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;

@Controller
@RequestMapping("/manage/customer")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	@GetMapping()
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Customer> customers = customerService.showAllCustomer(keyword, pageRequest);
		PageRender<Customer> pageRender = new PageRender<>("/manage/customer", customers);
//
		model.addAttribute("customerList", customers);
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("currentPage", page);
		return "customer/index";
	}

	@PostMapping
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String storeCustomer(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Customer customer)
			throws IOException {

		customerService.save(customer);

		model.addAttribute("message", "Lưu thông tin khách hàng thành công");

		return list(page, keyword, model);
	}

	@PostMapping("/update")
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public RedirectView updateCustomer(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Customer customer)
			throws IOException {
		
		customerService.save(customer);
		
		model.addAttribute("message", "Lưu thông tin khách hàng thành công");
		return new RedirectView("/manage/customer?page=" + page + "&key=" + keyword);
	}

	@GetMapping("/{id}")
	public @ResponseBody Customer showDetail(@PathVariable("id") Long id) {
		return customerService.findById(id);
	}

	@GetMapping("/delete/{id}")
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {
		Customer customer = customerService.findById(id);
		String message = customerService.delete(id);
		if (!message.isEmpty())
			model.addAttribute("message", message);
		return new RedirectView("/manage/customer?page=" + page + "&key=" + keyword);
	}
}
