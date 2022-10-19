package com.cp2196g03gr01.category;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cp2196g03gr01.common.CategoryLevelEnum;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.service.ICategoryService;
import com.cp2196g03gr01.service.impl.CategoryService;
import com.cp2196g03gr01.util.SlugHandler;
import com.github.javafaker.Faker;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
	@MockBean
	private ICategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	public void somethingTest() {
		Faker faker = new Faker();
		System.out.println(faker.food().ingredient());
		
	}

	@Test
	public void createCategory() {
		Faker faker = new Faker();
		
		Category category=new Category();
		
		String cName=faker.food().ingredient();
		
		category.setName(cName);
		category.setAlias(SlugHandler.toSlug(cName));
		category.setLevel(CategoryLevelEnum.MENU);		
		
		categoryService.createCategory(category);
	}
}
