package com.cp2196g03gr01.controller;

import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.apache.bcel.classfile.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.service.IUserService;

import net.bytebuddy.utility.RandomString;

@Controller
public class UserForgotPasswordController {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IUserService userService;

	@GetMapping("/forgot_password/employee")
	public String showForgotPasswordForm() {
		return "auth/forgotPassword";
	}

	@PostMapping("/forgot_password/employee")
	public String processForgotPassword(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
//		generate 6 digits token
		String token = RandomString.make(30);

		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
					+ "/reset_password/employee?token=" + token;
			sendEmail(email, resetPasswordLink);
			model.addAttribute("message",
					"Hệ thống đã gửi link đặt lại mật khẩu tới email của bạn. Vui lòng kiểm tra hộp thư đến hoặc thư rác");

		} catch (NoSuchElementException ex) {
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email");
		}

		return "auth/forgotPassword";
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("support@hdtpharmacy.com", "HDT Pharmacy");
		helper.setTo(recipientEmail);

		String subject = "[OTP] đặt lại mật khẩu HDT Pharmacy";

		String content = "<p>Xin chào,</p>" + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
				+ "<p>Vui lòng nhấn vào đường dẫn dưới đây để đặt lại mật khẩu:</p>" + "<p><a href=\"" + link
				+ "\">Đặt lại mật khẩu</a></p>" + "<br>" + "<p>Bỏ qua email này nếu bạn nhớ mật khẩu, "
				+ "hoặc người đã gửi yêu cầu không phải bạn.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@GetMapping("/reset_password/employee")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    User user = userService.getByResetPasswordToken(token);
	    model.addAttribute("token", token);
	     
	    if (user == null) {
	        model.addAttribute("message", "OTP không hợp lệ!");
	        return "message";
	    }
	     
	    return "auth/resetPassword";
	}

	@PostMapping("/reset_password/employee")
	public String processResetPassword(HttpServletRequest request, Model model) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     
	    User user = userService.getByResetPasswordToken(token);
	    model.addAttribute("title", "Reset your password");
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    } else {           
	        userService.updatePassword(user, password);
	         
	        model.addAttribute("message", "Đặt lại .");
	    }
	     
	    return "auth/changePasswordSuccess";
	}
}
