package com.IVSS.backend.controller;

import com.IVSS.backend.model.User;
import com.IVSS.backend.model.Video;
import com.IVSS.backend.repositories.UserRepository;
import com.IVSS.backend.service.MinioService;
import com.IVSS.backend.service.UpDownloadService;
import com.IVSS.backend.service.UserService;
import com.IVSS.backend.service.VideoService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "http://localhost:4200")
public class VideoController {

    @Autowired
    private UserService userService;
    @Autowired
    private final VideoService videoService;
    @Autowired
    private final MinioService minioService;
    @Autowired
    private final UpDownloadService updownloadServices;
    @Autowired
    private final UserRepository userRepository;


    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired
    public VideoController(VideoService videoService, MinioService minioService, UpDownloadService updownloadServices, UserRepository userRepository) {
        this.videoService = videoService;
        this.minioService = minioService;
        this.updownloadServices = updownloadServices;
        this.userRepository = userRepository;
    }

    public Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return  userService.getUserIdByEmail(username);
    }

    public ObjectNode jsonRes(String status, ObjectNode data){
        ObjectNode response = JsonNodeFactory.instance.objectNode();
//        response.put("id", id);
        response.put("status", status);
        response.put("data", data);
        return response;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadVideo(@RequestParam("file") MultipartFile file) {
        Long id =  getUserId();
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        try {
//            String fileName = minioService.uploadFile(file);
            String fileName = updownloadServices.uploadFile(file);
            System.out.println("Filename > " + fileName);
            Long vidId = videoService.uploadVideo(fileName, id);
            System.out.println("vidId > " + vidId);
            data.put("video_id", vidId);
            ObjectNode response = jsonRes("Upload successful", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.out.println(e);
            ObjectNode response = jsonRes("Upload failed", data);///test err
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/all")
    public ResponseEntity<ObjectNode> getAllVideos() {
        Long userId =  getUserId();
        User owner =  userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        try {
            int i=0;
            for (Video vid : owner.getVideos())
                data.put("video_id_" + String.valueOf(i++), vid.getId());
            ObjectNode response = jsonRes("retrieve videos successful", data);
            response.put("videos_count", String.valueOf(i));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/video/{videoId}")
    public ResponseEntity<Video> getVideoMetadata(@PathVariable Long videoId) {
        try {
            Video video = videoService.getVideoMetadata(videoId);

            return ResponseEntity.ok(video);
        } catch (NoSuchElementException e) {
//            System.o
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping("/video/{videoId}")
//    public ResponseEntity<ObjectNode> getVideoMetadata(@PathVariable Long videoId) {
//        try {
//            Video video = videoService.getVideoMetadata(videoId);
//            ObjectNode data = JsonNodeFactory.instance.objectNode();
//
//            data.put("D",  video.getFilePath());
//
//
//            ObjectNode response = jsonRes("retrieve video successful", data);
//            return ResponseEntity.ok(response);
//        } catch (NoSuchElementException e) {
////            System.o
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


    @PostMapping("/delete/{videoId}")
    public ResponseEntity<ObjectNode> deleteVideo(@PathVariable Long videoId) {
        Long userId = getUserId();
        try {
            Long video = videoService.deleteVideo(videoId, userId);
            ObjectNode response = jsonRes("video deleted successful", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/watch/{videoId}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable Long videoId) throws IOException {
        Long userId = getUserId();
        try {
            Video video = videoService.getVideo(videoId, userId);// err if videoId is string

            return updownloadServices.downloadFile(video.getFilePath());
//            Resource resource = resourceLoader.getResource("classpath:static/videos/ff3f2c27-d119-449e-816e-c38e7a115073.mp4");
//            System.out.println(resource);
//            return ResponseEntity.ok()
//                    .contentLength(resource.contentLength())
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("view/{videoId}")
    public ResponseEntity<ObjectNode> viewVideo(@PathVariable Long videoId) {
        Long userId = getUserId();
        try {
            Video video = videoService.markWatchedVideo(videoId, userId);
            ObjectNode response = jsonRes("video marked as successful", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @GetMapping("/{videoId}/download")
//    public ResponseEntity<InputStreamResource> downloadVideo(@PathVariable Long videoId) {
//        try {
//            InputStreamResource videoStream = videoService.downloadVideo(videoId);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(videoStream);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
