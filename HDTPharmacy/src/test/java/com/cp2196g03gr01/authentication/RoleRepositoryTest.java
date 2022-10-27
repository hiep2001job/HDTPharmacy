package com.cp2196g03gr01.authentication;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.common.RolesEnum;
import com.cp2196g03gr01.entity.Role;

import com.cp2196g03gr01.repository.IRoleRepository;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	@Autowired
	private IRoleRepository roleRepository;
	
	/*
	 * @Test public void createRole() { Role role=new Role();
	 * role.setName(RolesEnum.CUSTOMER);
	 * role.setDescription(RolesEnum.CUSTOMER.getValue()); Role
	 * resRole=roleRepository.save(role); }
	 */
}
