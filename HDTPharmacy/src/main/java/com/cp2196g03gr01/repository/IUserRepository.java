package com.cp2196g03gr01.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp2196g03gr01.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	
}
