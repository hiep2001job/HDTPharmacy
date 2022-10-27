package com.cp2196g03gr01.product;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cp2196g03gr01.entity.Product;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.repository.IProductRepository;
import com.cp2196g03gr01.service.IProductService;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
	
	@Autowired
	private IProductRepository productRepository;
	
	@Autowired
	private ICategoryRepository categoryRepository;

	@Test
	public void testCreate() {
		Product p = new Product();
		p.setName("Viên uống Nature's Bounty C 500Mg bổ sung vitamin (100 viên)");
		p.setAbsorbingWay("Uống");
		p.setWeight(40f);
		
		p.setCategory(categoryRepository.findAll(PageRequest.of(0,1)).getContent().get(0));

		Product res = productRepository.save(p);

	}

	@Test
	public void testRemove() {

		productRepository.deleteById(1L);
		assertTrue(!productRepository.findById(1L).isPresent());

	}
	@Test
	public void testSearch() {

		Page<Product> res=productRepository.findFullTextSearchByName("viên", PageRequest.of(0,5));
		System.out.println(res.getContent().size());

	}
	@Test
	public void testProductInformation() {

		System.out.println(productRepository.findById(12L).get().toString());
	}
	
	

}
