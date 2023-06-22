package com.IVSS.backend.controller;

import com.IVSS.backend.model.User;
import com.IVSS.backend.model.Video;
import com.IVSS.backend.repositories.UserRepository;
import com.IVSS.backend.repositories.VideoRepository;
import com.IVSS.backend.service.MinioService;
import com.IVSS.backend.service.UpDownloadService;
import com.IVSS.backend.service.UserService;
import com.IVSS.backend.service.VideoService;
import com.fasterxml.jackson.databind.JsonNode;
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
    @Autowired
    private final VideoRepository videoRepository;


    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired
    public VideoController(VideoService videoService, MinioService minioService, UpDownloadService updownloadServices, UserRepository userRepository,  VideoRepository videoRepository) {
        this.videoService = videoService;
        this.minioService = minioService;
        this.updownloadServices = updownloadServices;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
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

//    @PostMapping("/upload")
//    public ResponseEntity<Object> uploadVideo(@RequestParam("file") MultipartFile file) {
//        Long id =  getUserId();
//        ObjectNode data = JsonNodeFactory.instance.objectNode();
//        try {
////            String fileName = minioService.uploadFile(file);
//            String fileName = updownloadServices.uploadFile(file);
//            System.out.println("Filename > " + fileName);
//            Long vidId = videoService.uploadVideo(fileName, id);//save vid
//            System.out.println("vidId > " + vidId);
//            boolean sentDeep = videoService.sendToDeep(fileName, vidId);
//            data.put("video_raw", vidId);//can cause error
//            if(sentDeep){
//                Video vid = videoRepository.findById(vidId).orElseThrow(NoSuchElementException::new);
//                data.put("video_process_state", "finished");
//                data.put("video_process", vid.getProcessedFilePath());//edit dont return file path
//                if(vid.getFightImgPath() != null){
//                    ObjectNode fight = JsonNodeFactory.instance.objectNode();
//                    for(int i = 0; i< vid.getFightImgPath().size(); i++)
//                        fight.put("fight_"+i, vid.getFightImgPath().get(i));
//                    data.put("fight_images", fight);
//                }else data.put("fight_images", JsonNodeFactory.instance.objectNode());
//
//                if(vid.getFightImgPath() != null) {
//                    ObjectNode face = JsonNodeFactory.instance.objectNode();
//                    for (int i = 0; i < vid.getFaceImgPath().size(); i++)
//                        face.put("face_" + i, vid.getFaceImgPath().get(i));
//                    data.put("face_images", face);
//                }else data.put("face_images", JsonNodeFactory.instance.objectNode());
//            }
//            else{
//                data.put("video_process_state", "not finished");
//            }
//            ObjectNode response = jsonRes("Upload successful", data);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            System.out.println(e);
//            ObjectNode response = jsonRes("Upload failed", data);///test err
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }


    @PostMapping("/upload")
    public ResponseEntity<Object> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("model") String model) {
        Long id =  getUserId();
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        try {
            String fileName = updownloadServices.uploadFile(file);
            Long vidId = videoService.uploadVideo(fileName, id, file.getOriginalFilename());//save vid

            String deepRes = videoService.UploadVideoToDeep(fileName, vidId, model);
            System.out.println("model"+model);
            data.put("status", "video send to deep learning models successfully");
            data.put("msg", deepRes);//can cause error
            data.put("videoId", vidId);//can cause error
            ObjectNode response = jsonRes("Upload successful", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            data.put("msg", e.getMessage());//can cause error
            data.put("status", "video send to deep learning models successfully");
            ObjectNode response = jsonRes("Upload failed", data);///test err
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }//getProcessedVideo

    @PostMapping("/status/{videoId}")
    public ResponseEntity<Object> getUploadedVideoSatatus(@PathVariable Long videoId) {
        Long id =  getUserId();
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        try {
            JsonNode response = videoService.getProcessedVideo(videoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ObjectNode response = jsonRes("failed", data);///test err
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
//    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/watch/{videoId}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable Long videoId) throws IOException {
        Long userId = getUserId();
        try {
            Video video = videoService.getVideo(videoId, userId);// err if videoId is string
            System.out.println(video.getProcessedFilePath());
            //de2d7c49-2e4d-4be5-8e89-b13f872ef670-fall_2.mp4
//            return updownloadServices.downloadFile("output.mp4");
//            return updownloadServices.downloadFile("de2d7c49-2e4d-4be5-8e89-b13f872ef670-fall_2.mp4");
//            return updownloadServices.downloadFile("2aa6df66-9a3f-4e31-aa76-5470531e013d.mp4");
//            return updownloadServices.downloadFile(video.getRawFilePath());
            return updownloadServices.downloadFile(video.getProcessedFilePath());

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/watchs/{videoId}")
    public ResponseEntity<ObjectNode> downloadVideoS(@PathVariable Long videoId) throws IOException {
        Long userId = getUserId();
        try {
            Video video = videoService.getVideo(videoId, userId);// err if videoId is string
            System.out.println(video.getProcessedFilePath());
            //de2d7c49-2e4d-4be5-8e89-b13f872ef670-fall_2.mp4
//            return updownloadServices.downloadFile("output.mp4");
//            return updownloadServices.downloadFile("de2d7c49-2e4d-4be5-8e89-b13f872ef670-fall_2.mp4");
//            return updownloadServices.downloadFile("2aa6df66-9a3f-4e31-aa76-5470531e013d.mp4");
//            return updownloadServices.downloadFile(video.getRawFilePath());

            ObjectNode response = jsonRes(video.getProcessedFilePath(), null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



//    @GetMapping("/getImg/{videoId}/{imgPath}")
    @GetMapping("/getImg/{imgPath}")
    public ResponseEntity<Resource> getImages(@PathVariable String imgPath) throws IOException {
//        public ResponseEntity<Resource> getImgs(@PathVariable Long videoId, @PathVariable String imgPath) throws IOException {
        Long userId = getUserId();
        try {
//            Video video = videoService.getVideo(videoId, userId);// err if videoId is string
            return updownloadServices.downloadFile(imgPath);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//
//    @GetMapping("/images/{videoId}/{imgPath}")
//    public ResponseEntity<Resource> getImgs(@PathVariable Long videoId) throws IOException {
//        Long userId = getUserId();
//        try {
//            Video video = videoService.getVideo(videoId, userId);// err if videoId is string
//            System.out.println(video.getRawFilePath());
//
//            return updownloadServices.downloadFile(video.getRawFilePath());
////            return updownloadServices.downloadFile(video.getProcessedFilePath());
//
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            System.out.println(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @PostMapping("/watch/{videoId}")
//    public ObjectNode getImgsProcessed(@PathVariable Long videoId) throws IOException {
//        Long userId = getUserId();
//        try {
//            Video video = videoService.getVideo(videoId, userId);// err if videoId is string
//
//
//            data.put("video_raw", vidId);//can cause error
//            if(sentDeep){
//                Video vid = videoRepository.findById(vidId).orElseThrow(NoSuchElementException::new);
//                data.put("video_process_state", "finished");
//                data.put("video_process", vid.getProcessedFilePath());
//                ObjectNode fight = JsonNodeFactory.instance.objectNode();
//                for(int i = 0; i< vid.getFightImgPath().size(); i++)
//                    fight.put("fight_"+i, vid.getFightImgPath().get(i));
//                data.put("fight_images", fight);
//            }
//            else{
//                data.put("video_process_state", "not finished");
//            }
//            ObjectNode response = jsonRes("Upload successful", data);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            System.out.println(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

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
