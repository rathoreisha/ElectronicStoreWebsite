package com.shruteekatech.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadImage(MultipartFile file,String path) throws IOException;

    InputStream getResource(String filename,String path) throws FileNotFoundException;
}
