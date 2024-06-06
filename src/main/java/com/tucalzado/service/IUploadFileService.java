package com.tucalzado.service;


import com.tucalzado.models.entity.ImageUrl;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {
    ImageUrl save(MultipartFile file) throws IOException;
    boolean delete(String filename);
}
