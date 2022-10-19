package com.cp2196g03gr01.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.util.PageRender;
import com.cp2196g03gr01.util.SlugHandler;



@Controller
@RequestMapping("/manage/category")
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping()
	public  String list(@RequestParam(name = "page", defaultValue = "0") int page,Model model) {
	
		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Category> categories = categoryService.showAllCategory(pageRequest);
		PageRender<Category> pageRender = new PageRender<>("/manage/category", categories);
		
		model.addAttribute("categoryList", categories);
		model.addAttribute("page", pageRender);
		model.addAttribute("category", new Category());
		model.addAttribute("parents", categoryService.showAllCategory());
		model.addAttribute("currentPage", page);
		
		return "category/index" ;
	}
	
	@PostMapping
	public String storeCategory(@RequestParam(name = "currentPage", defaultValue = "0") int page,Model model,
			 @Valid Category category) {
		
		category.setAlias(SlugHandler.toSlug(category.getName()));
		categoryService.save(category);
		model.addAttribute("message", "Lưu phân loại thành công");
		return list(page,model);
	}
	
	@GetMapping("/{id}")
	public @ResponseBody Category showDetail(@PathVariable("id") Long id) {
		return categoryService.findById(id);
	}

	
	
	@GetMapping("/delete/{id}")
	public RedirectView delete(@RequestParam(name = "page", defaultValue = "0") int page,@PathVariable Long id,Model model) {
		String message = categoryService.delete(id);
		if(!message.isEmpty())
			model.addAttribute("message",message);
		return new RedirectView("/manage/category?page="+page);
	}
}
