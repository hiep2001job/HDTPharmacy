package com.cp2196g03gr01.customer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.entity.Customer;
import com.cp2196g03gr01.entity.Supplier;
import com.cp2196g03gr01.repository.ICustomerRepository;
import com.cp2196g03gr01.util.SlugHandler;
import com.github.javafaker.Faker;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
	@Autowired
	private ICustomerRepository customerRepository;
	
	@Test
	public void create() {
		Faker faker = new Faker();
		Customer customer;
		List<Customer> list = new ArrayList<Customer>();
		int i = 20;
		while (i-- > 0) {
			customer = new Customer();
			String name = faker.name().fullName();
			customer.setName(name);
			customer.setAddress(faker.address().fullAddress());
			customer.setPhone(faker.phoneNumber().cellPhone());
			list.add(customer);
		}
		customerRepository.saveAll(list);

	}
}
