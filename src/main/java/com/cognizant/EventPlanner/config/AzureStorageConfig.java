package com.cognizant.EventPlanner.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.cognizant.EventPlanner.config.properties.AzureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AzureStorageConfig {

    private final AzureProperties azureProperties;

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(azureProperties.getEndpoint())
                .sasToken(azureProperties.getSasToken())
                .buildClient();
    }
}
