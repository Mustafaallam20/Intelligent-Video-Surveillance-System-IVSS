package com.IVSS.backend.service;

import com.IVSS.backend.model.User;
import com.IVSS.backend.model.Video;
import com.IVSS.backend.repositories.UserRepository;
import com.IVSS.backend.repositories.VideoRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

//    private String baseDir = "./videos/";


    public VideoService(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }
    public List<Video> getAllVideo(Long userId){
        List<Video> videos = videoRepository.findByUser_id(userId);
        return videos;
    }

    public Long uploadVideo(String fileName, Long ownerId) throws IOException {
        System.out.println("parameter filename, id > " + fileName + ", "+ ownerId.toString());
        User owner =  userRepository.findById(ownerId).orElseThrow(NoSuchElementException::new);
        System.out.println("user name > " + owner.getName());
        Video video = new Video();
        video.setUser(owner);
//        video.setVideoLen(getVideoDuration(filePath));
//        video.setResolution(getVideoResolution(filePath));
        video.setFilePath(fileName);
        videoRepository.save(video);
        System.out.println("test");
        System.out.println(video.getId());
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
        String filePath = video.getFilePath();

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
}
