package com.cognizant.EventPlanner.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@RequiredArgsConstructor
public class Base64DecodedMultipartFileUtil implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    @Override
    @NonNull
    public String getName() {
        return UUID.randomUUID() + getFileExtension();
    }

    @Override
    public String getOriginalFilename() {
        return getName();
    }

    @Override
    public String getContentType() {
        return header.split(":")[1].split(";")[0];
    }

    private String getFileExtension() {
        String contentType = getContentType();
        if (contentType != null && contentType.contains("/")) {
            return "." + contentType.split("/")[1];
        }
        return "";
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte @NonNull [] getBytes() {
        return imgContent;
    }

    @Override
    @NonNull
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (InputStream inputStream = getInputStream()) {
            Files.copy(inputStream, dest.toPath());
        }
    }
}
