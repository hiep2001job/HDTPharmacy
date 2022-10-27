package com.cp2196g03gr01.configure;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("user-photos", registry);
		
		exposeDirectory("../category-images", registry);

		exposeDirectory("../user-images", registry);
		
		exposeDirectory("../product-images", registry);

		exposeDirectory("../supplier-images", registry);
	}
	
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path photoDir = Paths.get(pathPattern);
		String photoUploadPath = photoDir.toFile().getAbsolutePath();
	
		String logicalPath = pathPattern.replace("..", "") + ("/**");
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + photoUploadPath + "/");
	}
	
	@Bean
	public LayoutDialect layoutDialect() {
	  return new LayoutDialect(new GroupingStrategy());
	}
}
