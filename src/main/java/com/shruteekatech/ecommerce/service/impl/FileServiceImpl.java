package com.shruteekatech.ecommerce.service.impl;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.exception.BadApiException;
import com.shruteekatech.ecommerce.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * @param file
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
//        Filename
        String originalFilename = file.getOriginalFilename();
        log.info("Filename :{}",originalFilename);
//        abc.png
//        Random generate file
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filenamewithextension=filename+extension;
        String filepath=path+ File.separator+filenamewithextension;
        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg"))
        {
//               to save the file
            File folder = new File(path);
            if(!folder.exists())
            {
//                create folder
                 folder.mkdirs();
            }
            //upload file
              Files.copy(file.getInputStream(), Paths.get(filepath));
            return filenamewithextension;
        }

        else {
            throw  new BadApiException(extension+""+ AppConstant.FILE_MSG);
        }


    }

    /**
     * @param filename
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public InputStream getResource(String filename, String path) throws FileNotFoundException {

        String fullpath=filename+File.separator+path;
        InputStream inputStream=new FileInputStream(fullpath);

        return inputStream;
    }
}
