package com.IVSS.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class UpDownloadService {
    @Value("${updownload_path}")
    private String uploadDirectory;
    @Value("${deepURL}")
    private String flaskAppUrl;
//    @Value("${minio.bucketName}")
//    private String bucketName;
//
//    @Value("${upload.directory}")
//    private String uploadDirectory;
//
//    @Autowired
//    private MinioClient minioClient;

    public String getBaseDir(){
        return System.getProperty("user.dir");
    }
    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Save the file to the server
        File serverFile = new File(getBaseDir() + uploadDirectory + objectName); // base path
        try (FileOutputStream fos = new FileOutputStream(serverFile)) {
            fos.write(file.getBytes());
        }
        return objectName;
    }




    public ResponseEntity<Resource> downloadFile(String objectName) throws Exception {
        // Download the file from server
        File serverFile = new File(getBaseDir() + uploadDirectory + objectName);
        byte[] bytes = null;
        try (FileInputStream fis = new FileInputStream(serverFile)) {
            bytes = fis.readAllBytes();
        }
        ByteArrayResource resource = new ByteArrayResource(bytes);

        // Set response headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .body(resource);
    }

}
