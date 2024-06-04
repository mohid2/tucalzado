package com.tucalzado.service.impl;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.tucalzado.models.entity.ImageUrl;
import com.tucalzado.repository.IImageUrlRepository;
import com.tucalzado.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UploadFileServiceImpl implements IUploadFileService {
    private final IImageUrlRepository imageUrlRepository;

    private final static String BUCKET_NAME = "pruebajwt-8d391.appspot.com";
    @Value("${firebase_private_key}")
    private String privateKeyFirebase;

    public UploadFileServiceImpl(IImageUrlRepository imageUrlRepository) {
        this.imageUrlRepository = imageUrlRepository;
    }

    @Override
    public Resource load(String filename) throws MalformedURLException {
      return null;
    }
    @Override
    public ImageUrl save(MultipartFile image) throws IOException {
        InputStream credentialsStream = new ByteArrayInputStream(privateKeyFirebase.getBytes());
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        InputStream inputStream = image.getInputStream();
        String ext = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf("."));
        String nameImage = UUID.randomUUID().toString() + ext;

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of(BUCKET_NAME, nameImage);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        Blob blob = storage.create(blobInfo, inputStream);
        // Construir la URL de Firebase Storage manualmente
        String firebaseUrl = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/" + nameImage + "?alt=media";
        ImageUrl imageURL = new ImageUrl();
        imageURL.setUrl(firebaseUrl);
        return imageUrlRepository.save(imageURL);
    }

    @Override
    public boolean delete(String filename) {
        // Implementa la lógica para eliminar la imagen del Firebase Storage
        return false;
    }
}
