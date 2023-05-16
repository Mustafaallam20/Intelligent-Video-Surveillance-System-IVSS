package com.IVSS.backend.config;

import io.minio.MinioClient;
//import io.minio.errors.InvalidEndpointException;
//import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MinIOConfig {

    @Value("${MINIO_ROOT_USER}")
    private String user;

    @Value("${MINIO_ROOT_PASSWORD}")
    private String password;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Bean
    public MinioClient minioClient() throws MinioException {
        return new MinioClient.Builder()
                .endpoint(endpoint)
                .credentials(user, password)
                .build();
    }
}



