package com.cp2196g03gr01.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {
	@GetMapping("/login/employee")
	public String showEmployeeLoginForm() {
		return "auth/employeeLogin";
	}
	@GetMapping("/access-denied")
	public String loginError(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "auth/page-403";
	}
	@GetMapping("/logout")
	public RedirectView logout(HttpServletRequest request, Model model) {
        return new RedirectView("/login/employee");
	}
	
}
