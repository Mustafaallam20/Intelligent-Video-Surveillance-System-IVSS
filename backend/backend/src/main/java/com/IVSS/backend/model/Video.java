package com.IVSS.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(appliesTo = "`Video`")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, unique = true)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "`user`", referencedColumnName = "`ID`", nullable = false)
    private User user;
    @Column(name = "`label`")
    private String label;
    @Column(name = "`detection_types`")
    private List<String> detection_types;
    @Column(name = "`raw_file_path`", nullable = false)
    private String rawFilePath;
	@Column(name = "`processed_file_path`")
	private String processedFilePath;

	@ElementCollection
	@CollectionTable(name = "fight_img_paths", joinColumns = @JoinColumn(name = "fight_id"))
	@Column(name = "`fight_img_path`")
	private List <String> fightImgPath;


	@ElementCollection
	@CollectionTable(name = "face_img_path", joinColumns = @JoinColumn(name = "face_id"))
	@Column(name = "`face_img_path`")//can cause error
	private List <String> faceImgPath;
    @Column(name = "`upload_date`", nullable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();
    @Column(name = "`video_length`")
    private Duration videoLen;
    @Column(name = "`resolution`")
    private String resolution;
    @Column(name = "processed", nullable = false)
    private Boolean processed = false;
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getDetection_types() {
		return detection_types;
	}
	public void setDetection_types(List<String> detection_types) {
		this.detection_types = detection_types;
	}

	public LocalDateTime getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Duration getVideoLen() {
		return videoLen;
	}
	public void setVideoLen(Duration videoLen) {
		this.videoLen = videoLen;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public Boolean getProcessed() {
		return processed;
	}
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
	public Boolean getWatched() {
		return watched;
	}
	public void setWatched(Boolean watched) {
		this.watched = watched;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}



	public String getRawFilePath() {
		return rawFilePath;
	}

	public void setRawFilePath(String rawFilePath) {
		this.rawFilePath = rawFilePath;
	}

	public String getProcessedFilePath() {
		return processedFilePath;
	}

	public void setProcessedFilePath(String processedFilePath) {
		this.processedFilePath = processedFilePath;
	}

	public List<String> getFightImgPath() {return fightImgPath;}

	public void setFightImgPath(List<String> fightImgPath) {
		if(fightImgPath.isEmpty())return;
		this.fightImgPath = fightImgPath;
	}

	public List<String> getFaceImgPath() {
		return faceImgPath;
	}

	public void setFaceImgPath(List<String> faceImgPath) {
		if(faceImgPath.isEmpty())return;
		this.faceImgPath = faceImgPath;
	}


}