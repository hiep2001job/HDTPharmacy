package com.cp2196g03gr01.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

	@Test
	public void createRole() {
		List<Role> roles=new ArrayList<>();
		Role role = new Role();
		role.setName(RolesEnum.MANAGER);
		role.setDescription(RolesEnum.MANAGER.getValue());
		roles.add(role);
		
		role = new Role();
		role.setName(RolesEnum.EMPLOYEE);
		role.setDescription(RolesEnum.EMPLOYEE.getValue());
		roles.add(role);
		
		role = new Role();
		role.setName(RolesEnum.CUSTOMER);
		role.setDescription(RolesEnum.CUSTOMER.getValue());
		roles.add(role);
		
		roleRepository.saveAll(roles);
	}

}
