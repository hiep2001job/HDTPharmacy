package com.cp2196g03gr01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/manage")
public class HomeController {
	@GetMapping("/")
	public String hello() {
		String url=ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		return "index";
	}
	@GetMapping("/login")
	public String login() {
		return "auth/employeeLogin";
	}
}
