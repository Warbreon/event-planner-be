package com.cognizant.EventPlanner.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.cognizant.EventPlanner.util.Base64DecodedMultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final BlobServiceClient blobServiceClient;
    private static final String CONTAINER_NAME = "image";

    public String uploadImageToAzure(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Base64 image string is required");
        }

        MultipartFile image = decodeBase64toMultipartFile(base64Image);

        String imageName = image.getOriginalFilename();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        containerClient.createIfNotExists();

        BlobClient blobClient = containerClient.getBlobClient(imageName);

        try {
            blobClient.upload(image.getInputStream(), image.getSize(), true);
        } catch (IOException e) {
            throw new IOException("Failed to upload image to azure", e);
        }

        return blobClient.getBlobUrl();
    }

    private MultipartFile decodeBase64toMultipartFile(String base64Image) {
        String[] parts = base64Image.split(",");
        byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
        return new Base64DecodedMultipartFileUtil(imageBytes, parts[0]);
    }
}
