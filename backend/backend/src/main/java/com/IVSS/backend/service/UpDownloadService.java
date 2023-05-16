package com.IVSS.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class UpDownloadService {
    @Value("${updownload_path}")
    private String uploadDirectory;
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

        // Prepare the HTTP headers and entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", new FileSystemResource(serverFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make an HTTP POST request to the Flask app's /process_video endpoint
        RestTemplate restTemplate = new RestTemplate();
        String flaskAppUrl = "http://localhost:5000/process_video";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(flaskAppUrl, requestEntity, String.class);

        // Retrieve the response from the Flask app
        String response = responseEntity.getBody();

//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        String response = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

// Access the JSON data using the JsonNode API
        String state = rootNode.get("state").asText();
        String video = rootNode.get("video").asText();

        // Access the JSON array using the JsonNode API
        JsonNode imagesNode = rootNode.get("images");
        if (imagesNode.isArray()) {
            for (JsonNode imageNode : imagesNode) {
                String data = imageNode.get("data").asText();
                String name = imageNode.get("name").asText();
                byte[] decodedImg = Base64.getDecoder().decode(video);
                String imgName = UUID.randomUUID().toString() + ".jpg";
                try (FileOutputStream outputStream = new FileOutputStream(imgName)) {
                    outputStream.write(decodedImg);
                    //add img to db //edit path
                }
            }
        }

        byte[] decodedVideo = Base64.getDecoder().decode(video);
        String vidName = UUID.randomUUID().toString() + ".mp4";
        // Write the decoded video bytes to a file
        try (FileOutputStream outputStream = new FileOutputStream(vidName)) {
            outputStream.write(decodedVideo);
            //add vid to db //edit path
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
