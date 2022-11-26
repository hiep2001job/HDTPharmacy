package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

import com.cp2196g03gr01.common.AzureBlobContainerEnum;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.service.IAzureBlobService;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.util.AzureBlobImageUri;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.FileHelper;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;
import com.cp2196g03gr01.util.UUIDHelper;

@Controller
@RequestMapping("/manage/category")
public class CategoryController {

	@Autowired
	private IAzureBlobService azureBlobService;

	@Autowired
	private ICategoryService categoryService;

	/* View category list */
	@GetMapping()
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

//		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Category> categories = categoryService.showAllCategory(keyword, PageRequest.of(page, 5));
		PageRender<Category> pageRender = new PageRender<>("/manage/category", categories);
//
		model.addAttribute("categoryList", categories);
		model.addAttribute("parents", categoryService.showAllCategory());
//		Pagination
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("currentPage", page);

		return "category/index";
	}

	/* Create category */
	@PostMapping
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Category category,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		/* Upload image */
		if (!file.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(category.getName()) + UUIDHelper.makeUUID() + "."
					+ FileHelper.getExtendsion(file);

			String fileName = StringUtils.cleanPath(formatFileName);

			category.setImage(fileName);

			CompletableFuture<URI> result = azureBlobService.upload(file.getBytes(), fileName,
					AzureBlobContainerEnum.CATEGORY_IMAGE.getValue());
			result.join();
		}
		category.setAlias(SlugHandler.toSlug(category.getName()));

		categoryService.save(category);

		model.addAttribute("message", "Lưu phân loại thành công");

		return list(page, keyword, model);
	}

	/* Update category */
	@PostMapping("/update")
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public RedirectView updateCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid Category category,
			@RequestParam("categoryImage") MultipartFile file) throws IOException {

		/* Update image */
		if (!file.isEmpty()) {
			List<CompletableFuture> futureList = new ArrayList<CompletableFuture>();
			/* Remove old image */
			if (category.getImage() != null && !category.getImage().isEmpty()) {
				String oldImage = category.getImage();
				CompletableFuture<String> deleteResult = azureBlobService
						.deleteBlob(AzureBlobContainerEnum.CATEGORY_IMAGE.getValue(), oldImage);
				futureList.add(deleteResult);
			}

			/* Upload new image */
			String formatFileName = SlugHandler.toSlug(category.getName()) + UUIDHelper.makeUUID() + "."
					+ FileHelper.getExtendsion(file);
			String fileName = StringUtils.cleanPath(formatFileName);

			category.setImage(fileName);

			CompletableFuture<URI> uploadResult = azureBlobService.upload(file.getBytes(), fileName,
					AzureBlobContainerEnum.CATEGORY_IMAGE.getValue());
			futureList.add(uploadResult);
			
			futureList.stream().forEach(CompletableFuture::join);

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
			azureBlobService.deleteBlob(AzureBlobContainerEnum.CATEGORY_IMAGE.getValue(), category.getImage());
		}
		String message = categoryService.delete(id);
		if (!message.isEmpty())
			model.addAttribute("message", message);
		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
	}
}
