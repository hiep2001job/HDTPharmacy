package com.cp2196g03gr01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cp2196g03gr01.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	User findByEmailAndIsEnableTrue(String email);

	User findByResetPasswordToken(String resetPasswordToken);

//	Page<User> findByUserFullnameContainsOrEmailContains(String userFullname,Pageable pageable);
	
	Page<User> findByUserFullnameLikeOrEmailLike(String userFullname, String email, Pageable pageable);	
	
	Page<User> findByUserFullnameContainsOrEmailContains(String userFullname, String email, Pageable pageable);

	Page<User> findByEmailContains(String userFullname, Pageable pageable);

	Boolean existsByEmail(String email);
}
