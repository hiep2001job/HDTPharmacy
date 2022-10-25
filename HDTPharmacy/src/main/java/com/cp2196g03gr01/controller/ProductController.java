package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.service.IProductService;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;

@Controller
@RequestMapping("/manage/product")
public class ProductController {
	@Autowired
	private IProductService productService;

	@Autowired
	private ICategoryService categoryService;

	@GetMapping
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Product> products = productService.showAllProduct(keyword, pageRequest);
		PageRender<Product> pageRender = new PageRender<>("/manage/product", products);

		model.addAttribute("categoryList", categoryService.showAllCategory());
		model.addAttribute("productList", products);
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);

		model.addAttribute("currentPage", page);
		return "product/index";
	}

	@PostMapping
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, Product product
			,@RequestParam("primaryImage") MultipartFile primaryImage
			,@RequestParam("imageFile1") MultipartFile image1
			,@RequestParam("imageFile2") MultipartFile image2
			,@RequestParam("imageFile3") MultipartFile image3
			,@RequestParam("imageFile4") MultipartFile image4) throws IOException {

		if (!primaryImage.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(product.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ primaryImage.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImagePrimary(fileName);

			String uploadDir = "../product-images/";

			FileHandler.saveFile(uploadDir, fileName, primaryImage);
		}
		if (!image1.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(product.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ image1.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage1(fileName);

			String uploadDir = "../product-images/";

			FileHandler.saveFile(uploadDir, fileName, image1);
		}
		if (!image2.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(product.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ image2.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage2(fileName);

			String uploadDir = "../product-images/";

			FileHandler.saveFile(uploadDir, fileName, image2);
		}
		if (!image3.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(product.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ image3.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage3(fileName);

			String uploadDir = "../product-images/";

			FileHandler.saveFile(uploadDir, fileName, image3);
		}
		if (!image4.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(product.getName())
					+ String.format("%04d", new Random().nextInt(10000)) + "."
					+ image4.getOriginalFilename().split("\\.")[1];
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage4(fileName);

			String uploadDir = "../product-images/";

			FileHandler.saveFile(uploadDir, fileName, image4);
		}

		product.setAlias(SlugHandler.toSlug(product.getName())+String.format("%04d", new Random().nextInt(10000)));
		productService.save(product);
		model.addAttribute("message", "Lưu phân loại thành công");

		return list(page, keyword, model);
	}
	
	@GetMapping("/delete/{id}")
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {
		Product product = productService.findById(id);
		List<String> imgPathList=product.getPhotoPathList();
		if (!imgPathList.isEmpty()) {
			for(String path:imgPathList)
			String dirCategory = "../category-images/" + category.getImage();
			FileHandler.removeDir(dirCategory);
		}

		String message = categoryService.delete(id);
		if (!message.isEmpty())
			model.addAttribute("message", message);
		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
	}
}
