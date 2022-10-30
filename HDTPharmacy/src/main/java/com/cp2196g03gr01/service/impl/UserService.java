package com.cp2196g03gr01.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.repository.IUserRepository;
import com.cp2196g03gr01.service.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	private IUserRepository userRepository;

	@Override
	public User getByResetPasswordToken(String token) {
		return userRepository.findByResetPasswordToken(token);
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws NoSuchElementException {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		} else {
			throw new NoSuchElementException("Could not find any user with the email " + email);
		}

	}

	@Override
	public void updatePassword(User user, String newPassword) {
		try {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);
			user.setResetPasswordToken(null);
			userRepository.save(user);
			System.out.println(user);
			System.out.println(newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public User findById(Long userId) {
		
		return  userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
	}

}
