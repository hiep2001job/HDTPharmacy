package com.cp2196g03gr01.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.repository.IUserRepository;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@Autowired
	private IUserRepository userRepository;

	@Test
	public void testSearchUser() {
//		List<User> result = userRepository.findByUserFullnameLikeOrEmailLike("ano","thanh", PageRequest.of(0, 5))
//				.getContent();
//		result.stream().forEach(System.out::println);

		List<User> result = userRepository.findByUserFullnameContainsOrEmailContains("ano", "thanh", PageRequest.of(0, 5))
				.getContent();
		result.stream().forEach(System.out::println);
	}

	@Test
	public void testUserIsExistByEmail() {
		Boolean result = userRepository.existsByEmail("@gmail.com");
		System.out.println("Is Exist: " + result);
	}
}
