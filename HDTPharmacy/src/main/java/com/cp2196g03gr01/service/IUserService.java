package com.cp2196g03gr01.service;

import java.util.NoSuchElementException;

import com.cp2196g03gr01.entity.User;

public interface IUserService {
	 public User getByResetPasswordToken(String token) ;
	 public void updateResetPasswordToken(String token, String email) throws NoSuchElementException ;
	 public void updatePassword(User user, String newPassword) ;
	public User findById(Long userId);
}
