package com.cp2196g03gr01.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp2196g03gr01.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {
	
}
