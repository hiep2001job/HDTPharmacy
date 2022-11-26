package com.cp2196g03gr01.util;

import org.springframework.web.multipart.MultipartFile;

public class FileHelper {
	
	public static String getExtendsion(MultipartFile file) {
		return file.getOriginalFilename().split("\\.")[1];
	}
}
