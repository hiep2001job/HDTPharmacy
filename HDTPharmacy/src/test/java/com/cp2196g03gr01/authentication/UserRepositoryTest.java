package com.cp2196g03gr01.authentication;

import java.util.HashSet;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.entity.Role;
import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.repository.IRoleRepository;
import com.cp2196g03gr01.repository.IUserRepository;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IRoleRepository iRoleRepository;
	
	@Test
	public void createUser() {
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

		
		Role managerRole=iRoleRepository.findById(2L).get();		
		User user =new User();
		user.setEmail("nthiepa19050@cusc.ctu.edu.vn");
		user.setIsEnable(true);
		user.setUserFullname("John Doe");
		user.setPhone("0362550693");		
		user.getRoles().add(managerRole);
		user.setPassword(bCryptPasswordEncoder.encode("123456"));
		
		userRepository.save(user);
	}
	@Test
	public void createUserManager() {
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

		
		Role managerRole=iRoleRepository.findById(1L).get();		
		User user =new User();
		user.setEmail("thanhhiep77777@gmail.com");
		user.setIsEnable(true);
		user.setUserFullname("Nguyễn Thành Hiệp");
		user.setPhone("0362550694");		
		user.getRoles().add(managerRole);
		user.setPassword(bCryptPasswordEncoder.encode("123456"));
		
		userRepository.save(user);
	}
	@Test
	public void showUser() {
		User user=userRepository.findByEmail("thanhhiep77777@gmail.com");
		System.out.println(user);
	}
	@Test
	public void testToken() {
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.encode("hehe"));
	}
	@Test
	public void testOtp() {
		System.out.println(String.format("%06d", new Random().nextInt(1000000)));
	}
	
}
