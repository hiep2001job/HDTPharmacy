package com.cp2196g03gr01.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.cp2196g03gr01.common.CategoryLevelEnum;
import com.cp2196g03gr01.entity.Category;
import com.cp2196g03gr01.repository.ICategoryRepository;
import com.cp2196g03gr01.util.SlugHandler;
import com.github.javafaker.Faker;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositotyTest {
	@Autowired
	private ICategoryRepository categoryRepository;

	@Test
	public void createCategory() {
		Faker faker = new Faker();
		Category category = new Category();
		String cName = faker.food().ingredient();

		category.setName(cName);
		category.setAlias(SlugHandler.toSlug(cName));
		category.setLevel(CategoryLevelEnum.MENU);

		categoryRepository.save(category);
	}

	@Test
	public void getHierachicalyParent() {
		Category cate=categoryRepository.findById(36L).get();
		System.out.println(cate.getParentName());
	}
	@Test
	public void createCategoryWithSubcatego() {
		Faker faker = new Faker();
		
		Category categoryParent = new Category();
		List<Category> subCateList = new ArrayList<>();

		String cParentName = faker.food().ingredient();
		categoryParent.setName(cParentName);
		categoryParent.setAlias(SlugHandler.toSlug(cParentName));
		categoryParent.setLevel(CategoryLevelEnum.MENU);
		categoryRepository.save(categoryParent);

		for (int i = 0; i <= 5; i++) {
			String cName = faker.food().ingredient();
			Category category = new Category();
			category.setName(cName);
			category.setAlias(SlugHandler.toSlug(cName));
			category.setLevel(CategoryLevelEnum.SUBMENU);
			category.setParentCategory(categoryParent);
			subCateList.add(category);
		}
		categoryRepository.saveAll(subCateList);
	}
	
	@Test
	public void createCategoryAsCategory() {
		Faker faker = new Faker();
		Optional<Category> categoryParentRes = categoryRepository.findById(6L);
		if(categoryParentRes.isPresent()) {
			Category parent=categoryParentRes.get();
			List<Category> subCateList = new ArrayList<>();

			
				String cName = faker.food().ingredient();
				Category category = new Category();
				category.setName(cName+" createCategoryAsCategory");
				category.setAlias(SlugHandler.toSlug(cName));
				category.setLevel(CategoryLevelEnum.LIST);
				category.setParentCategory(parent);
				subCateList.add(category);
			
			categoryRepository.saveAll(subCateList);
			
		}

		
	}
	
	@Test 
	public void  ftsCategoryByName() {
		List<Category> list=categoryRepository.findFullTextSearchByName("sinh thiết");
		for(Category cat:list) {
			System.out.println(cat.getName());
		}
	}

	@Test
	public void listCategoryHierachicaly() {
		List<Category> menuCategoryList = categoryRepository.findByParentCategoryIsNull();
		for (Category category : menuCategoryList) {
			printCategoryTree(category);
		}
	}

	private void printCategoryTree(Category category) {
		
		if (category.getParentCategory()!=null) System.out.print("---");
		System.out.println(category.getName());		
		
		int subCategoryListSize = category.getChildCategories().size();
		if (subCategoryListSize > 0) {
			for (Category subcate : category.getChildCategories()) {
				printCategoryTree(subcate);
			}
		}
	}

}
