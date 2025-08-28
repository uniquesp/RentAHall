package com.rentahall.hallservice.service.impl;

import com.rentahall.hallservice.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadHallImage(MultipartFile file, UUID hallId, UUID imageId) {
        try {
            // 1. Check if bucket exists, if not create
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build()
            );
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build()
                );
            }

            // 2. Generate object name -> halls/{hallId}/{imageId}-filename
            String objectName = "halls/" + hallId + "/" + imageId + "-" + file.getOriginalFilename();

            // 3. Upload file to bucket
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 4. Return the accessible URL
            return minioConfig.getEndpoint() + "/" + minioConfig.getBucket() + "/" + objectName;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage(), e);
        }
    }
}