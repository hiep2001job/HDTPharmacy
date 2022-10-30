package com.cp2196g03gr01.retail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.repository.IRetailRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RetailRepositoryTest {
	@Autowired
	private IRetailRepository repo;

	@Test
	public void testOrderFinding() {
		System.out.println(repo.findAllByOrderByIdDesc(PageRequest.of(0, 8)).getContent().get(0));
	}
}
