package com.cp2196g03gr01.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageReadParam;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.common.AzureBlobContainerEnum;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.service.IAzureBlobService;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.service.IProductService;
import com.cp2196g03gr01.util.FileHandler;
import com.cp2196g03gr01.util.FileHelper;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;
import com.cp2196g03gr01.util.UUIDHelper;

@Controller
@RequestMapping("/manage/product")
public class ProductController {
	private final String PRODUCT_IMAGE_PATH = "../product-images/";

	@Autowired
	private IAzureBlobService azureBlobService;

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
	public RedirectView store(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, Product product,
			@RequestParam("primaryImage") MultipartFile primaryImage, @RequestParam("imageFile1") MultipartFile image1,
			@RequestParam("imageFile2") MultipartFile image2, @RequestParam("imageFile3") MultipartFile image3,
			@RequestParam("imageFile4") MultipartFile image4, RedirectAttributes flash) throws IOException {

		/* Create alias */
		String alias = SlugHandler.toSlug(product.getName());

		/* Init CompletableFuture list */
		List<CompletableFuture<URI>> futureList = new ArrayList<>();

		/* Upload images list */
		if (!primaryImage.isEmpty()) {
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(primaryImage);
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImagePrimary(fileName);
			CompletableFuture<URI> result = azureBlobService.upload(primaryImage.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(result);
		}
		if (!image1.isEmpty()) {
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image1);
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage1(fileName);
			CompletableFuture<URI> result = azureBlobService.upload(image1.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(result);
		}
		if (!image2.isEmpty()) {
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image2);
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage2(fileName);
			CompletableFuture<URI> result = azureBlobService.upload(image2.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(result);
		}
		if (!image3.isEmpty()) {
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image3);
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage3(fileName);
			CompletableFuture<URI> result = azureBlobService.upload(image3.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(result);
		}
		if (!image4.isEmpty()) {
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image4);
			String fileName = StringUtils.cleanPath(formatFileName);
			product.setImage4(fileName);
			CompletableFuture<URI> result = azureBlobService.upload(image4.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(result);
		}
		/* Other information */
		product.setAlias(alias + UUIDHelper.makeUUID());

		/* Wait for all upload process to be completed */
		futureList.forEach(CompletableFuture::join);

		productService.save(product);
		flash.addAttribute("message", "Tạo sản phẩm thành công");

		return new RedirectView("/manage/product?page=" + page + "&key=" + keyword);
	}

	/* Update product */
	@PostMapping("/update")
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public String update(@RequestParam(name = "currentPage", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, Model model, Product product,
			@RequestParam("primaryImage") MultipartFile primaryImage, @RequestParam("imageFile1") MultipartFile image1,
			@RequestParam("imageFile2") MultipartFile image2, @RequestParam("imageFile3") MultipartFile image3,
			@RequestParam("imageFile4") MultipartFile image4) throws IOException {

		String alias = SlugHandler.toSlug(product.getName());

		/* Update images */
		List<CompletableFuture> futureList = new ArrayList<CompletableFuture>();

		if (!primaryImage.isEmpty()) {
			/* remove old image */
			String oldPrimaryImage = product.getImagePrimary();
			CompletableFuture<String> deleteResult = azureBlobService
					.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), oldPrimaryImage);
			futureList.add(deleteResult);

			/* save new image */
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(primaryImage);
			String fileName = StringUtils.cleanPath(formatFileName);

			product.setImagePrimary(fileName);
			CompletableFuture<URI> uploadResult = azureBlobService.upload(primaryImage.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(uploadResult);
		}
		if (!image1.isEmpty()) {
			/* remove old image */
			String oldImage1 = product.getImage1();
			CompletableFuture<String> deleteResult = azureBlobService
					.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), oldImage1);
			futureList.add(deleteResult);

			/* save new image */
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image1);
			String fileName = StringUtils.cleanPath(formatFileName);

			product.setImagePrimary(fileName);
			CompletableFuture<URI> uploadResult = azureBlobService.upload(image1.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(uploadResult);
		}
		if (!image2.isEmpty()) {
			/* remove old image */
			String oldImage2 = product.getImage2();
			CompletableFuture<String> deleteResult = azureBlobService
					.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), oldImage2);
			futureList.add(deleteResult);

			/* save new image */
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image2);
			String fileName = StringUtils.cleanPath(formatFileName);

			product.setImagePrimary(fileName);
			CompletableFuture<URI> uploadResult = azureBlobService.upload(image2.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(uploadResult);
		}
		if (!image3.isEmpty()) {
			/* remove old image */
			String oldImage3 = product.getImage3();
			CompletableFuture<String> deleteResult = azureBlobService
					.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), oldImage3);
			futureList.add(deleteResult);

			/* save new image */
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image3);
			String fileName = StringUtils.cleanPath(formatFileName);

			product.setImagePrimary(fileName);
			CompletableFuture<URI> uploadResult = azureBlobService.upload(image3.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(uploadResult);
		}
		if (!image4.isEmpty()) {
			/* remove old image */
			String oldImage4 = product.getImage4();
			CompletableFuture<String> deleteResult = azureBlobService
					.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), oldImage4);
			futureList.add(deleteResult);

			/* save new image */
			String formatFileName = alias + UUIDHelper.makeUUID() + "." + FileHelper.getExtendsion(image4);
			String fileName = StringUtils.cleanPath(formatFileName);

			product.setImagePrimary(fileName);
			CompletableFuture<URI> uploadResult = azureBlobService.upload(image4.getBytes(), fileName,
					AzureBlobContainerEnum.PRODUCT_IMAGE.getValue());
			futureList.add(uploadResult);
		}
		futureList.stream().forEach(CompletableFuture::join);

		product.setAlias(alias + UUIDHelper.makeUUID());

		productService.save(product);

		model.addAttribute("message", "Lưu phân loại thành công");

		return list(page, keyword, model);
	}

	/* Get product information to fill update form */
	@GetMapping("/{id}")
	public @ResponseBody Product showDetail(@PathVariable("id") Long id) {
		return productService.findById(id);
	}

	/* Delete product */
	@GetMapping("/delete/{id}")
	@Transactional(rollbackOn = { IOException.class, PersistenceException.class })
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "key", defaultValue = "") String keyword, @PathVariable Long id, Model model) {

		Product product = productService.findById(id);
		/* Init CompletableFuture list */
		List<CompletableFuture<String>> futureList = new ArrayList<>();

		/* Delete images from storage service asynchronously */
		List<String> imgPathList = product.getAllImage();
		imgPathList.stream().forEach(image -> {
			azureBlobService.deleteBlob(AzureBlobContainerEnum.PRODUCT_IMAGE.getValue(), image);
		});

		productService.delete(id);

		return new RedirectView("/manage/product?page=" + page + "&key=" + keyword);
	}
}
