package com.cp2196g03gr01.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		// get path folder to upload file
		Path uploadPath = Paths.get(uploadDir);

		// if path upload folder not exist
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new IOException("Could not save file: " + fileName, e);
		}
	}

	public static void clearDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Could not list directory :" + dirPath);
		}
	}

	public static void removeDir(String dir) {
//		clearDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (Exception e) {
			System.out.println("Could not delete directory: " + dir);
		}
	}
}
