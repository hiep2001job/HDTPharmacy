package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.common.AzureBlobContainerEnum;
import com.cp2196g03gr01.dto.UserEditProfileDTO;
import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.security.HDTUserDetail;
import com.cp2196g03gr01.service.IAzureBlobService;
import com.cp2196g03gr01.service.IUserService;
import com.cp2196g03gr01.util.FileHelper;
import com.cp2196g03gr01.util.SlugHandler;
import com.cp2196g03gr01.util.UUIDHelper;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private IAzureBlobService azureBlobService;

	@Autowired
	private IUserService userService;

	/* Show employee profile */

	@GetMapping()
	public String showProfile(@AuthenticationPrincipal HDTUserDetail userDetail, Model model) {
		User user = userService.findById(userDetail.getId());
		user.setPassword(null);
		model.addAttribute("user", user);
		return "employee/profile";
	}

	/* Update user profile */
	@PostMapping()
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public RedirectView updateCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model,
			@AuthenticationPrincipal HDTUserDetail userDetail, @Valid UserEditProfileDTO employee,
			@RequestParam("avatar") MultipartFile file) throws IOException {

		User user = userService.findById(employee.getId());
//		Map update information
		user.setUserFullname(employee.getUserFullName());
		user.setPhone(employee.getPhone());
		user.setAddress(employee.getAddress());
		List<CompletableFuture> futureList = new ArrayList<CompletableFuture>();

		/* Update image */
		if (!file.isEmpty()) {
			/* Remove old image */
			if (user.getImage() != null && !user.getImage().isEmpty()) {
				String oldImage = user.getImage();

				CompletableFuture<String> deleteResult = azureBlobService
						.deleteBlob(AzureBlobContainerEnum.USER_IMAGE.getValue(), oldImage);
				futureList.add(deleteResult);
			}
			/* Upload new image */
			String formatFileName = SlugHandler.toSlug(employee.getUserFullName()) + UUIDHelper.makeUUID() + "."
					+ FileHelper.getExtendsion(file);
			String fileName = StringUtils.cleanPath(formatFileName);

			CompletableFuture<URI> uploadResult = azureBlobService.upload(file.getBytes(), fileName,
					AzureBlobContainerEnum.USER_IMAGE.getValue());
			futureList.add(uploadResult);
			user.setImage(fileName);
		}
		User updatedUser = userService.save(user);
		userDetail.refreshUserDetail(updatedUser);

		if (!futureList.isEmpty())
			futureList.stream().forEach(CompletableFuture::join);

		return new RedirectView("/profile");
	}
}
