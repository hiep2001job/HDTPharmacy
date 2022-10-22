package com.cp2196g03gr01.controller;

import java.io.IOException;

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
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;

@Controller
@RequestMapping("/manage/category")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	@GetMapping()
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Category> categories = categoryService.showAllCategory(keyword, pageRequest);
		PageRender<Category> pageRender = new PageRender<>("/manage/category", categories);

		model.addAttribute("categoryList", categories);
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("category", new Category());
		model.addAttribute("parents", categoryService.showAllCategory());
		model.addAttribute("currentPage", page);

		return "category/index";
	}

	@PostMapping
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Category category,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(category.getName()) + file.getOriginalFilename().split("\\.")[1];
			;
			String fileName = StringUtils.cleanPath(formatFileName);
			category.setImage(fileName);

			String uploadDir = "../category-images/";
			FileHandler.clearDir(uploadDir);
			FileHandler.saveFile(uploadDir, fileName, file);
		}

		category.setAlias(SlugHandler.toSlug(category.getName()));
		categoryService.save(category);
		model.addAttribute("message", "Lưu phân loại thành công");
		return list(page, keyword, model);
	}

	@PostMapping("/update")
	public RedirectView updateCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Category category,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
//			remove old image
			String dirCategory = "../category-images/" + category.getImage();
			FileHandler.removeDir(dirCategory);
//			Upload new image
			String formatFileName = SlugHandler.toSlug(category.getName()) + "."
					+ file.getOriginalFilename().split("\\.")[1];
			;
			String fileName = StringUtils.cleanPath(formatFileName);
			category.setImage(fileName);

			String uploadDir = "../category-images/";
			FileHandler.saveFile(uploadDir, fileName, file);
		}

		category.setAlias(SlugHandler.toSlug(category.getName()));
		categoryService.save(category);
		model.addAttribute("message", "Lưu phân loại thành công");
		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
	}

	@GetMapping("/{id}")
	public @ResponseBody Category showDetail(@PathVariable("id") Long id) {
		return categoryService.findById(id);
	}

	@GetMapping("/delete/{id}")
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {
		Category category = categoryService.findById(id);
		if (category.getImage() != null) {
			String dirCategory = "../category-images/" + category.getImage();
			FileHandler.removeDir(dirCategory);
		}

		String message = categoryService.delete(id);
		if (!message.isEmpty())
			model.addAttribute("message", message);
		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
	}
}
