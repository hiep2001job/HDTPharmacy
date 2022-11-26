package com.cp2196g03gr01.service;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cp2196g03gr01.entity.User;

public interface IUserService {
	
	public User getByResetPasswordToken(String token);

	public void updateResetPasswordToken(String token, String email) throws NoSuchElementException;

	public void updatePassword(User user, String newPassword);

	public User findById(Long userId);
	
	public Page<User> showAllUser(String key, Pageable pageable);
	
	public User save(@Valid User user);
	
	public Boolean isEmailExist(String email);
}
