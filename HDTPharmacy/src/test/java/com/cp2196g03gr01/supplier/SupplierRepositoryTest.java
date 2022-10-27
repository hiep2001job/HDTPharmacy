package com.cp2196g03gr01.supplier;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.entity.Supplier;
import com.cp2196g03gr01.repository.ISupplierRepository;
import com.cp2196g03gr01.util.SlugHandler;
import com.github.javafaker.Faker;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SupplierRepositoryTest {

	@Autowired
	private ISupplierRepository supplierRepository;

	@Test
	public void createSupplier() {
		Faker faker = new Faker();
		Supplier supplier;
		List<Supplier> list = new ArrayList<Supplier>();
		int i = 10;
		while (i-- > 0) {
			supplier = new Supplier();
			String name = faker.name().fullName();
			supplier.setName(name);
			supplier.setAddress(faker.address().fullAddress());
			supplier.setPhone(faker.phoneNumber().cellPhone());
			supplier.setAlias(SlugHandler.toSlug(name));
			list.add(supplier);
		}
		supplierRepository.saveAll(list);

	}

	@Test
	public void findSupplier() {
//		List<Supplier> list= supplierRepository.findAll();
//		for(Supplier s:list) {
//			System.out.println(s);
//		}
		Page<Supplier> list = supplierRepository.findByNameContains("A", PageRequest.of(0, 5));
		for (Supplier s : list) {
			System.out.println(s);
		}
	}
}
