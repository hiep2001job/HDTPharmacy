package com.cp2196g03gr01.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AzureBlobImageUri {
	
	private String blobContainerName;
	private String blobName;
	
	public AzureBlobImageUri(String uri) {
		String[] path = uri.split("/");
		blobName=path[path.length-1];
		blobContainerName=path[path.length-2];
	}
}
