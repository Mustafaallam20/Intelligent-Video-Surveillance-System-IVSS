package com.IVSS.backend.service;

import com.IVSS.backend.model.User;
import com.IVSS.backend.model.Video;
import com.IVSS.backend.repositories.UserRepository;
import com.IVSS.backend.repositories.VideoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class VideoService {

    @Value("${deepURL}")
    private String deepAppUrl;

    @Value("${updownload_path}")
    private String uploadDirectory;

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

//    private String baseDir = "./videos/";

    public String getBaseDir(){
        return System.getProperty("user.dir");
    }


    public VideoService(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }
    public List<Video> getAllVideo(Long userId){
        List<Video> videos = videoRepository.findByUser_id(userId);
        return videos;
    }

    public Long uploadVideo(String fileName, Long ownerId, String originalFilename) throws IOException {
//        System.out.println("parameter filename, id > " + fileName + ", "+ ownerId.toString());
        User owner =  userRepository.findById(ownerId).orElseThrow(NoSuchElementException::new);
//        System.out.println("user name > " + owner.getName());
        Video video = new Video();
        video.setUser(owner);
//        video.setVideoLen(getVideoDuration(filePath));
//        video.setResolution(getVideoResolution(filePath));
        video.setRawFilePath(fileName);
        video.setTitle(originalFilename);

//        System.out.println("RawFilePath");
        videoRepository.save(video);
//        System.out.println("test");
//        System.out.println(video.getId());
        return video.getId();
        //try
    }

    public Long deleteVideo(Long id, Long ownerId) throws IOException {
        // Find the video by its ID
        Video video = videoRepository.findById(id).orElseThrow(NoSuchElementException::new);
        // Check if the video's owner ID matches the specified owner ID
        if (!video.getUser().getId().equals(ownerId)) {
//            throw new UnauthorizedAccessException("You are not authorized to delete this video");
//            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
        // Delete the video by setting the 'deleted' flag to true
        video.setDeleted(true);
        // Save the updated video to the database
        videoRepository.save(video);
        return video.getId();
    }

    public Video getVideo(Long id, Long ownerId) throws IOException {
        // Find the video by its ID
        Video video = videoRepository.findById(id).orElseThrow(NoSuchElementException::new);
        // Check if the video's owner ID matches the specified owner ID
        if (!video.getUser().getId().equals(ownerId)) {
//            throw new UnauthorizedAccessException("You are not authorized to delete this video");
//            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }

        return video;
    }
    public Video markWatchedVideo(Long id, Long ownerId) throws IOException {
        // Find the video by its ID
        Video video = videoRepository.findById(id).orElseThrow(NoSuchElementException::new);
        // Check if the video's owner ID matches the specified owner ID
        if (!video.getUser().getId().equals(ownerId)) {
//            throw new UnauthorizedAccessException("You are not authorized to delete this video");
//            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
        // Delete the video by setting the 'deleted' flag to true
        video.setWatched(true);
        // Save the updated video to the database
        videoRepository.save(video);
        return video;
    }

    public Video getVideoMetadata(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(NoSuchElementException::new);
    }

    public InputStreamResource downloadVideo(Long videoId) throws IOException {
        // Get the video file path from the database
        Video video = videoRepository.findById(videoId).orElseThrow(NoSuchElementException::new);
        String filePath = video.getRawFilePath();

        // Return an InputStreamResource for the video file
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        return new InputStreamResource(inputStream);
    }

    private String saveVideoFile(MultipartFile file) throws IOException {
        // Generate a random file name for the video file
        String fileName =  UUID.randomUUID().toString() + ".mp4";

        // Save the file to disk
        Path filePath = (Path) Paths.get("src/main/resources/static/videos/",fileName);

//        Path filePath = Paths.get("classpath:static/videos", fileName);
        System.out.println(filePath.toAbsolutePath());

        Files.copy(file.getInputStream(), filePath);

        return filePath.toAbsolutePath().toString();
    }


//    private Duration getVideoDuration(String filePath) throws IOException {
//        // Use FFmpeg to get the video duration
//        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
//        FFmpegProbeResult probeResult = ffprobe.probe(filePath);
//        return probeResult.getFormat().duration;
//    }
//
//    private String getVideoResolution(String filePath) throws IOException {
//        // Use FFmpeg to get the video resolution
//        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
//        FFmpegProbeResult probeResult = ffprobe.probe(filePath);
//        return probeResult.getStreams().get(0).width + "x" + probeResult.getStreams().get(0).height;
//    }


    public boolean sendToDeep(String vidPath, Long vidId) throws IOException {
        Video videoObj = videoRepository.findById(vidId).get();
        // Prepare the HTTP headers and entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-model", "fight");//edit fight to general
        System.out.println("q1: "+ vidPath);
        File videoFile = new File(getBaseDir() + uploadDirectory + vidPath);
        System.out.println("q2");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        System.out.println("q3");
        body.add("video", new FileSystemResource(videoFile));
        System.out.println("q4");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        System.out.println("q5");

        // Make an HTTP POST request to the Flask app's /process_video endpoint
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("q6");

        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(deepAppUrl, requestEntity, String.class);
            System.out.println("q7");
            // Retrieve the response from the Flask app
            String response = responseEntity.getBody();
            System.out.println("q8");
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("q9");
            JsonNode rootNode = objectMapper.readTree(response);
            System.out.println("q10");
// Access the JSON data using the JsonNode API
//            String state = rootNode.get("state").asText();
            String video = rootNode.get("video").asText();
            System.out.println("q11");

            // Access the JSON array using the JsonNode API
            JsonNode imagesNode = rootNode.get("fight_images");
            System.out.println("q12");
            if (imagesNode.isArray()) {
                System.out.println("q13");
                List <String> imgPaths = new ArrayList<>();;//can cause error if null
                System.out.println("q14");
                for (JsonNode imageNode : imagesNode) {
                    System.out.println("q15");
                    String data = imageNode.get("data").asText();
                    System.out.println("q15+");
//                String name = imageNode.get("name").asText();
                    byte[] decodedImg = Base64.getDecoder().decode(data);
                    System.out.println("q16");
                    String imgName = imageNode.get("name") + "---" + UUID.randomUUID().toString() + ".jpg";//new File(getBaseDir() + uploadDirectory + vidPath);
                    System.out.println("q17");
                    try (FileOutputStream outputStream = new FileOutputStream(getBaseDir() + uploadDirectory + imgName)) {
                        outputStream.write(decodedImg);
                        //add img to db //edit path
                        imgPaths.add(imgName);
                    } catch (IOException e) {//handel exception
                        System.out.println("q18: "+ e);
                        return false;
//                        throw new RuntimeException(e);
                    }
                }
                videoObj.setFightImgPath(imgPaths);
            }

            imagesNode = rootNode.get("face_images");
            if (imagesNode.isArray()) {
                List <String> imgPaths = new ArrayList<>();;//can cause error if null
                for (JsonNode imageNode : imagesNode) {
                    String data = imageNode.get("data").asText();
//                String name = imageNode.get("name").asText();
                    byte[] decodedImg = Base64.getDecoder().decode(data);
                    String imgName = imageNode.get("name") +"---"+ UUID.randomUUID().toString() + ".jpg";//new File(getBaseDir() + uploadDirectory + vidPath);
                    try (FileOutputStream outputStream = new FileOutputStream(getBaseDir() + uploadDirectory + imgName)) {
                        outputStream.write(decodedImg);
                        //add img to db //edit path
                        imgPaths.add(imgName);
                    } catch (IOException e) {//handel exception
                        return false;
//                        throw new RuntimeException(e);
                    }
                }
                videoObj.setFaceImgPath(imgPaths);
            }

            byte[] decodedVideo = Base64.getDecoder().decode(video);
            String vidName = UUID.randomUUID().toString() + ".mp4";
            // Write the decoded video bytes to a file
            try (FileOutputStream outputStream = new FileOutputStream(getBaseDir() + uploadDirectory + vidName)) {
                outputStream.write(decodedVideo);
                videoObj.setProcessedFilePath(vidName);

            } catch (IOException e) {//handel exception
                System.out.println("q: "+ e);
                return false;
//                throw new RuntimeException(e);
            }
        }
        catch (Exception e){
            System.out.println("qq: "+ e);
            return false;
        }
        videoRepository.save(videoObj);
        return true;
    }







    public String UploadVideoToDeep(String vidPath, Long vidId, String model) throws IOException {
        Video videoObj = videoRepository.findById(vidId).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-model", model);
        headers.set("X-id", vidId.toString());
        File videoFile = new File(getBaseDir() + uploadDirectory + vidPath);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", new FileSystemResource(videoFile));
//        body.add("id", vidId);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(deepAppUrl+"/input", requestEntity, String.class);
            String response = responseEntity.getBody();
            int statusCode = responseEntity.getStatusCodeValue();
            return response;
        }
        catch (Exception e){
            return e.getMessage();
        }

    }

    public JsonNode getProcessedVideo(Long vidId) throws IOException {
        ObjectNode res = JsonNodeFactory.instance.objectNode();
//        Video videoObj = videoRepository.findById(vidId).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("video", new FileSystemResource(videoFile));
//        body.add("id", videoObj.getId());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make an HTTP POST request to the Flask app's /process_video endpoint
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(deepAppUrl + "/response", requestEntity, String.class);
            // Retrieve the response from the Flask app
            String response = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            Video videoObj = videoRepository.findById(rootNode.get("id").asLong()).get();
// Access the JSON data using the JsonNode API
//            String state = rootNode.get("state").asText();
            res.put("status", rootNode.get("status"));
            res.put("percent", rootNode.get("percent"));
            res.put("trigger", rootNode.get("trigger"));
            res.put("msg", rootNode.get("msg"));

            String status = rootNode.get("status").asText();
            System.out.println("112: " + status);
            if (status.equals("processing") || status.equals("failed")) {
                System.out.println("113: " + status == "processing" || status=="failed");

                System.out.println(res);
                return res;
            }
            System.out.println("q11: " + status + status.equals("finished"));
//            if (status == "finished") {
//                return res;
//            }

            if (status.equals("finished")) {
                JsonNode data = rootNode.get("data");
                System.out.println("p170");
                String video = data.get("video").asText();
                System.out.println("p171");
                byte[] decodedVideo = Base64.getDecoder().decode(video);
                System.out.println("p172");
                String vidName = UUID.randomUUID().toString() + ".mp4";//not mp4

                File serverFile2 = new File("/Users/muhammadeid/Documents/GitHub/Intelligent-Video-Surveillance-System-IVSS/front-end/src/assets/videos/" + vidName); // base path
                try (FileOutputStream fos = new FileOutputStream(serverFile2)) {
                    fos.write(decodedVideo);
                }

                System.out.println("p173:" + vidName);
                try (FileOutputStream outputStream = new FileOutputStream(getBaseDir() + uploadDirectory + vidName)) {
                    outputStream.write(decodedVideo);
                    System.out.println("p174");
                    System.out.println("setProcessedFilePath"+vidName);
                    videoObj.setProcessedFilePath(vidName);
                    System.out.println("setProcessedFilePath"+vidName);

                } catch (IOException e) {//handel exception
                    System.out.println("p175: " + e.getMessage());
                    System.out.println("p175: " + e.toString());
                    res.put("error", e.getMessage());/////////////
                }
                System.out.println("q11");
                System.out.println("setProcessedFilePath has been set");


                // Access the JSON array using the JsonNode API
                JsonNode imagesNode = data.get("images");
                System.out.println("q12");
                if (imagesNode.isArray()) {
                    System.out.println("q13");
                    List<String> fall = new ArrayList<>();
                    List<String> fight = new ArrayList<>();
                    List<String> face = new ArrayList<>(); //can cause error if null
                    List<String> crash = new ArrayList<>();


                    System.out.println("q14");
                    for (JsonNode imageNode : imagesNode) {
                        String imgName = imageNode.get("name").asText() + "---" + UUID.randomUUID().toString() + ".jpg";//new File(getBaseDir() + uploadDirectory + vidPath);
                        String detectType = imageNode.get("type").asText();
                        String img = imageNode.get("data").asText();
                        byte[] decodedImg = Base64.getDecoder().decode(img);
                        try (FileOutputStream outputStream = new FileOutputStream(getBaseDir() + uploadDirectory + imgName)) {
                            outputStream.write(decodedImg);
                            //add img to db //edit path
                            switch (detectType) {
                                case "fall":
                                    fall.add(imgName);
                                    break;
                                case "fight":
                                    fight.add(imgName);
                                    break;
                                case "face":
                                    face.add(imgName);
                                    break;
                                case "crash":
                                    crash.add(imgName);
                                    break;
                            }

                        } catch (IOException e) {//handel exception
                            res.put("error", e.getMessage());/////////////
                            return res;
    //                        throw new RuntimeException(e);
                        }
                    }
                    videoObj.setFallImgPath(fall);
                    videoObj.setFightImgPath(fight);
                    videoObj.setFaceImgPath(face);
                    videoObj.setCrashImgPath(crash);
                }
                videoRepository.save(videoObj);
            }
        }
        catch (Exception e) {
            res.put("error", e.toString());/////////////
            return res;
        }
        return res;
    }
}
