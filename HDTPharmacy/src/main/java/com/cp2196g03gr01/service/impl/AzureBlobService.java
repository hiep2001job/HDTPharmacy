package com.cp2196g03gr01.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cp2196g03gr01.service.IAzureBlobService;
import com.cp2196g03gr01.util.AzureBlobImageUri;
import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import groovy.util.logging.Slf4j;

@Service
@Slf4j
public class AzureBlobService implements IAzureBlobService {

	@Autowired
	private CloudBlobClient cloudBlobClient;

	@Override
	public boolean createContainer(String containerName) {
		boolean containerCreated = false;
		CloudBlobContainer container = null;
		try {
			container = cloudBlobClient.getContainerReference(containerName);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
		try {
			containerCreated = container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER,
					new BlobRequestOptions(), new OperationContext());
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return containerCreated;
	}

	@Override
	@Async
	public CompletableFuture<URI> upload(byte[] byteStream, String blobName, String blobContainer) {
		URI uri = null;
		CloudBlockBlob blob = null;
		CloudBlobContainer container = null;
		try {
			/* Create blob container if not existed */
			container = cloudBlobClient.getContainerReference(blobContainer);
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());
			/* Upload blob */
			blob = container.getBlockBlobReference(blobName);
			blob.upload(new ByteArrayInputStream(byteStream), -1);
			uri = blob.getUri();
			return CompletableFuture.completedFuture(uri);
		} catch (URISyntaxException | StorageException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<URI> listBlobs(String containerName) {
		List<URI> uris = new ArrayList<>();
		try {
			CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
			for (ListBlobItem blobItem : container.listBlobs()) {
				uris.add(blobItem.getUri());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return uris;
	}

	@Override
	@Async
	public CompletableFuture<String> deleteBlob(String containerName, String blobName) {
		try {
			CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
			CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
			blobToBeDeleted.deleteIfExists();
			return CompletableFuture.completedFuture("");
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ByteArrayOutputStream download(String containerName, String blobitem) {
		try {
			CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
			CloudBlockBlob blob = container.getBlockBlobReference(blobitem);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			blob.download(os);
			return os;
		} catch (URISyntaxException | StorageException e) {
			return null;
		}

	}

}
