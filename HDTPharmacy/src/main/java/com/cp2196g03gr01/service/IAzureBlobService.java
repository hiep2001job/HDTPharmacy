package com.cp2196g03gr01.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.BlobRequestOptions;

public interface IAzureBlobService {
	
	public boolean createContainer(String containerName);
				
	public CompletableFuture<URI> upload(byte[] bs, String blobName, String blobContainer);
	
	public List<URI> listBlobs(String containerName);
	
	public CompletableFuture<String> deleteBlob(String container,String blob);
	
	public ByteArrayOutputStream download(String containerName, String blobitem);
}
