package com.cognizant.EventPlanner.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint("https://dbgliveuwsourcerysa.blob.core.windows.net/")
                .sasToken("sv=2022-11-02&ss=bfqt&srt=c&sp=rwdlacupitfx&se=2024-06-30T14:40:34Z&st=2024-04-30T06:40:34Z&spr=https&sig=ugZWVijbmey%2FEhkG4N9h%2BDr79LH%2BEi9R%2FIjkV9pT5nA%3D")
                .buildClient();
    }
}
