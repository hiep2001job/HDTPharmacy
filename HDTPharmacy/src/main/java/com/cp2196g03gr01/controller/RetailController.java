package com.cp2196g03gr01.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cp2196g03gr01.component.IAuthenticationFacade;
import com.cp2196g03gr01.dto.ProductDTO;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.entity.RetailInvoice;
import com.cp2196g03gr01.entity.RetailInvoiceDetail;
import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.exception.ProductOutOfStockException;
import com.cp2196g03gr01.projection.ICustomerProjection;
import com.cp2196g03gr01.projection.IProductProjection;
import com.cp2196g03gr01.repository.IRetailRepository;
import com.cp2196g03gr01.security.HDTUserDetail;
import com.cp2196g03gr01.service.ICustomerService;
import com.cp2196g03gr01.service.IProductService;
import com.cp2196g03gr01.service.IRetailService;
import com.cp2196g03gr01.service.IUserService;
import com.cp2196g03gr01.util.PageRender;

@Controller
public class RetailController {

	@Autowired
	private IProductService productService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IRetailService retailService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	/* View retail invoice list */
	@GetMapping("/manage/all-retail")
	public String manageRetail(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, Locale locale) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<RetailInvoice> invoiceList = retailService.showAllRetailInvoice(keyword, pageRequest);
		PageRender<RetailInvoice> pageRender = new PageRender<>("/manage/category", invoiceList);
		model.addAttribute("invoiceList", invoiceList);

//		Pagination
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("currentPage", page);

		return "retail/manage";
	}

	@GetMapping("/retail")
	public String showRetailHome() {
		return "retail/index";
	}

	@GetMapping("/retail/invoice/{id}")
	public @ResponseBody ResponseEntity<?> showInvoice(@PathVariable("id") Long id) {
		return ResponseEntity.ok(retailService.findById(id));
	}

	@GetMapping("/retail/invoice/{id}/detail")
	public String showInvoiceDetail(@PathVariable("id") Long id, Model model) {
		RetailInvoice invoice = retailService.findById(id);
		model.addAttribute("invoice", invoice);
		return "retail/detail";
	}

	/* ----- Save Invoice ----- */
	@PostMapping("/retail/create")
	@Transactional(rollbackOn = { ProductOutOfStockException.class })
	public String save(RetailInvoice invoice, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "amount[]", required = false) Integer[] amount, RedirectAttributes flash,
			SessionStatus status, Locale locale) throws ProductOutOfStockException {
		/* Validate */
		boolean hasError = false;
		if (result.hasErrors()) {
			hasError = true;
			flash.addFlashAttribute("title", "Hóa đơn không hợp lệ!");
		}

		if (itemId == null || itemId.length == 0) {
			hasError = true;
			flash.addFlashAttribute("title", "Hóa đơn không hợp lệ!");
			flash.addFlashAttribute("message", "Chưa nhập thông tin sản phẩm");
		}
		if (hasError)
			return "redirect:/retail";

		Integer sumAmount = Arrays.asList(amount).stream().reduce(0, (a, b) -> a + b);
		if (sumAmount.intValue() <= 0) {
			hasError = true;
			flash.addFlashAttribute("title", "Hóa đơn không hợp lệ!");
			flash.addFlashAttribute("message", "Số lượng sản phẩm không hợp lệ");
		}
		if (hasError)
			return "redirect:/retail";

		/* Set invoice creator */
		HDTUserDetail principal = (HDTUserDetail) authenticationFacade.getAuthentication().getPrincipal();
		Long userId = principal.getId();
		User user = userService.findById(userId);
		invoice.setUser(user);

		/* Set invoices items */
		for (int i = 0; i < itemId.length; i++) {
			Product product = productService.findById(itemId[i]);

			RetailInvoiceDetail item = new RetailInvoiceDetail();
			item.setAmount(amount[i]);
			item.setProduct(product);
			invoice.addItemInvoice(item);

			/* Check stock quantity */
			int remainingStock = product.getStock() - item.getAmount();

			if (remainingStock < 0)
				throw new ProductOutOfStockException();

			product.setStock(product.getStock() - item.getAmount());

			productService.save(product);
		}

		/* Calculate customer reward points */
		Long rewardPoint = invoice.getTotal() / 10000;
		Customer customer = invoice.getCustomer();
		if (customer.getEarnedPoint() == null)
			customer.setEarnedPoint(0L);
		customer.setEarnedPoint(customer.getEarnedPoint() + rewardPoint);

		invoice = retailService.save(invoice);

		status.setComplete();

		flash.addFlashAttribute("title", "Tạo hóa đơn thành công");

		return "redirect:/retail/invoice/" + invoice.getId() + "/detail";
	}

	/* Find products for auto completing */
	@GetMapping(value = "/retail/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<IProductProjection> loadProducts(@PathVariable String term) {
		return productService.findSuggestProuduct(term, PageRequest.of(0, 8)).getContent();
	}

	/* Find customer for auto completing */
	@GetMapping(value = "/retail/load-customers/{term}", produces = { "application/json" })
	public @ResponseBody List<ICustomerProjection> loadCustomers(@PathVariable String term) {
		if (term.length() < 2)
			return new ArrayList<>();
		return customerService.searchCustomer(term, PageRequest.of(0, 8)).getContent();
	}
}
