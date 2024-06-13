package com.tucalzado.service.impl;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.tucalzado.models.entity.ImageUrl;
import com.tucalzado.repository.IImageUrlRepository;
import com.tucalzado.service.IUploadFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UploadFileServiceImpl implements IUploadFileService {
    private final IImageUrlRepository imageUrlRepository;

    private final static String BUCKET_NAME = "pruebajwt-8d391.appspot.com";
    @Value("${firebase_private_key}")
    private String privateKeyFirebase;

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
//        return imageUrlRepository.save(imageURL);
        return imageURL;
    }

    @Override
    public boolean delete(String  fileUrl) {
        try {
            // Obtener las credenciales desde la cadena privada almacenada
            InputStream credentialsStream = new ByteArrayInputStream(privateKeyFirebase.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

            // Crear el servicio de Storage utilizando las credenciales
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            // Extraer el nombre del archivo de la URL completa
            String filename = extractFilenameFromUrl(fileUrl);
            if (filename == null) {
                return false;
            }

            // Construir el BlobId utilizando el nombre del archivo y el nombre del bucket
            BlobId blobId = BlobId.of(BUCKET_NAME, filename);
            // Eliminar el archivo del bucket
            boolean deleted = storage.delete(blobId);
            // Verificar si la eliminación fue exitosa
            if (deleted) {
                // Si fue exitoso, también puedes eliminar la entrada correspondiente de tu base de datos
                // asumiendo que tienes una relación entre el nombre del archivo y el registro en tu repositorio
                Optional<ImageUrl> imageUrlOptional = imageUrlRepository.findByUrlContaining(filename);
                imageUrlOptional.ifPresent(imageUrlRepository::delete);
                return true;
            } else {
                // Si no se pudo eliminar, puedes manejar el error según sea necesario
                return false;
            }
        } catch (IOException e) {
            // Manejar excepciones de IO según sea necesario
            e.printStackTrace();
            return false;
        }
    }

    // Extraer el nombre del archivo de la URL completa
    private String extractFilenameFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
