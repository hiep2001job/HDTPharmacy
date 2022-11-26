package com.cp2196g03gr01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cp2196g03gr01.service.IAzureBlobService;


@RestController
@RequestMapping("/api/blob")
public class AzureBlobController {
	@Autowired
	private IAzureBlobService azureBlobService;
	
	/* Stream a blob to octec stream */
	@GetMapping("/{container}/{blob}")
	public byte[] download(@PathVariable String container,@PathVariable String blob) {
		return azureBlobService.download(container,blob).toByteArray();
	}
}
