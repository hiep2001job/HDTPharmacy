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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Supplier;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.service.ISupplierService;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;

@Controller
@RequestMapping("/manage/supplier")
public class SupplierController {

	@Autowired
	private ISupplierService supplierService;

	@GetMapping()
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Supplier> suppliers = supplierService.showAllSupplier(keyword, pageRequest);
		PageRender<Supplier> pageRender = new PageRender<>("/manage/supplier", suppliers);
//
		model.addAttribute("supplierList", suppliers);

//		Pagination
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("currentPage", page);

		return "supplier/index";
	}

	@PostMapping
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Supplier supplier,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(supplier.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ file.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			supplier.setImage(fileName);

			String uploadDir = "../supplier-images/";

			FileHandler.saveFile(uploadDir, fileName, file);
		}

		supplier.setAlias(SlugHandler.toSlug(supplier.getName()));

		supplierService.save(supplier);

		model.addAttribute("message", "Lưu nhà cung cấp thành công");

		return list(page, keyword, model);
	}

	@PostMapping("/update")
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public RedirectView updateCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Supplier supplier,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
//			remove old image
			String oldImg = "../supplier-images/" + supplier.getImage();
			FileHandler.removeDir(oldImg);
//			Upload new image
			String formatFileName = SlugHandler.toSlug(supplier.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ file.getOriginalFilename().split("\\.")[1];
			;
			String fileName = StringUtils.cleanPath(formatFileName);
			String uploadDir = "../category-images/";
			FileHandler.saveFile(uploadDir, fileName, file);
			supplier.setImage(fileName);
		}

		supplier.setAlias(SlugHandler.toSlug(supplier.getName()));
		supplierService.save(supplier);
		model.addAttribute("message", "Lưu nhà cung cấp thành công");
		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
	}

	@GetMapping("/{id}")
	public @ResponseBody Supplier showDetail(@PathVariable("id") Long id) {
		return supplierService.findById(id);
	}

	@GetMapping("/delete/{id}")
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {
		Supplier supplier = supplierService.findById(id);
		if (supplier.getImage() != null) {
			String dirsupplier = "../supplier-images/" + supplier.getImage();
			FileHandler.removeDir(dirsupplier);
		}

		String message = supplierService.delete(id);
		if (!message.isEmpty())
			model.addAttribute("message", message);
		return new RedirectView("/manage/supplier?page=" + page + "&key=" + keyword);
	}
}
