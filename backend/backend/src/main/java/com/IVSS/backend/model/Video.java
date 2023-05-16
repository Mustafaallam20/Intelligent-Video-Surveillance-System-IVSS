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
    @Column(name = "`file_path`", nullable = false)
    private String filePath;
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


}