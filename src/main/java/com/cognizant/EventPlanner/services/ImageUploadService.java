package com.cognizant.EventPlanner.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final BlobServiceClient blobServiceClient;

    public String uploadImageToAzure(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String containerName = "image";
        String imageName = image.getOriginalFilename();
//        BlobClient blobClient = blobServiceClient
//                .getBlobContainerClient("event-images")
//                .getBlobClient(imageName);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.createIfNotExists();

        BlobClient blobClient = containerClient.getBlobClient(imageName);

        try {
            blobClient.upload(image.getInputStream(), image.getSize(), true);
            System.out.println("Image uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload image");
            throw new IOException("Failed to upload image to azure", e);
        }

        return blobClient.getBlobUrl();
    }
}
