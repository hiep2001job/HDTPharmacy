package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.cp2196g03gr01.dto.UserEditProfileDTO;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.security.HDTUserDetail;
import com.cp2196g03gr01.service.IAzureBlobService;
import com.cp2196g03gr01.service.IUserService;
import com.cp2196g03gr01.service.impl.AzureBlobService;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.FileHelper;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;
import com.cp2196g03gr01.util.UUIDHelper;

@Controller
@RequestMapping("/manage/employee")
public class EmployeeController {

	private final String USER_IMAGE_PATH = "../user-images/";

	@Autowired
	private IAzureBlobService azureBlobService;

	@Autowired
	private IUserService userService;

	/* View employee list */
	@GetMapping()
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<User> users = userService.showAllUser(keyword, pageRequest);
		PageRender<User> pageRender = new PageRender<>("/manage/employee", users);

		model.addAttribute("employeeList", users);

//		Pagination
		model.addAttribute("page", pageRender);
		model.addAttribute("key", keyword);
		model.addAttribute("currentPage", page);

		return "employee/index";
	}

	/* Check Employee email exists */
	@GetMapping("/emailExist")
	public @ResponseBody Boolean checkEmailExists(@RequestParam("email") String email) {
		return userService.isEmailExist(email);
	}

	/* Create employee */
	@PostMapping
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, @Valid User employee,
			@RequestParam("employeeImage") MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
			String formatFileName = SlugHandler.toSlug(employee.getUserFullname()) + UUIDHelper.makeUUID() + "."
					+ FileHelper.getExtendsion(file);

			String fileName = StringUtils.cleanPath(formatFileName);

			employee.setImage(fileName);

			CompletableFuture<URI> result = azureBlobService.upload(file.getBytes(), fileName,
					AzureBlobContainerEnum.USER_IMAGE.getValue());
			result.join();
		}

		userService.save(employee);

		model.addAttribute("message", "Tạo nhân viên thành công");

		return list(page, keyword, model);
	}

	
//
//	@GetMapping("/{id}")
//	public @ResponseBody Category showDetail(@PathVariable("id") Long id) {
//		return categoryService.findById(id);
//	}
//	@GetMapping("/delete/{id}")
//	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
//			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {
//		Category category = categoryService.findById(id);
//		if (category.getImage() != null) {
//			String dirCategory = "../category-images/" + category.getImage();
//			FileHandler.removeDir(dirCategory);
//		}
//
//		String message = categoryService.delete(id);
//		if (!message.isEmpty())
//			model.addAttribute("message", message);
//		return new RedirectView("/manage/category?page=" + page + "&key=" + keyword);
//	}
}
