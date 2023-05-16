package com.IVSS.backend.repositories;

import com.IVSS.backend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUser_id(Long userId);

//    @Query("SELECT id FROM Video WHERE ownerId = :ownerId")
//    List<Video> getVideosByUserId(@Param("ownerId") Long id);
//    List<Video> getVideos(Long userId);

//    List<Video> findByWatched(boolean watched);

//    List<Video> findByDeleted(boolean deleted);

//    List<Video> findByProcessingTime(Duration processingTime);

//    List<Video> findByUploadTime(LocalDateTime uploadTime);

}

