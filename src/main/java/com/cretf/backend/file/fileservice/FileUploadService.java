package com.cretf.backend.file.fileservice;

import com.cretf.backend.file.dto.FilesDTO;
import com.cretf.backend.file.entity.Files;
import com.cretf.backend.product.repository.FilesRepository;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FileUploadService {

    private final MinioClient minioClient;
    private final FilesRepository filesRepository;

    @Value("${minio.bucket}")
    private String bucket;

    public FileUploadService(MinioClient minioClient, FilesRepository filesRepository) {
        this.minioClient = minioClient;
        this.filesRepository = filesRepository;
    }

    // Upload file
    public FilesDTO uploadFile(MultipartFile file) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        String path = fileId + "/" + fileName;

        // Upload to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(path)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(fileType)
                        .build()
        );

        // Save metadata to database
        Files fileEntity = new Files();
        fileEntity.setFileId(fileId);
        fileEntity.setName(fileName);
        fileEntity.setPath(path);
        fileEntity.setType(fileType);

        filesRepository.save(fileEntity);

        return new FilesDTO(fileId, fileName, path, fileType);
    }

    //mutiple upload
    public List<FilesDTO> uploadFileMulti(MultipartFile[] files) throws Exception{
        List<FilesDTO> retVal = Arrays.stream(files).map(f -> {
            try {
                return uploadFile(f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return retVal;
    }

    // Get file by ID
    public Optional<FilesDTO> getFileById(String fileId) {
        return filesRepository.findById(fileId)
                .map(file -> new FilesDTO(
                        file.getFileId(),
                        file.getName(),
                        file.getPath(),
                        file.getType()
                ));
    }

    // Generate presigned URL for file access
    public String generatePresignedUrl(String path) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(path)
                        .method(Method.GET)
                        .expiry(24, TimeUnit.HOURS) // URL có hiệu lực trong 24 giờ
                        .build()
        );
    }

    // Delete file
    public void deleteFile(String fileId) throws Exception {
        Optional<Files> fileOptional = filesRepository.findById(fileId);
        if (fileOptional.isPresent()) {
            Files file = fileOptional.get();

            // Delete from MinIO
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(file.getPath())
                            .build()
            );

            // Delete from database
            filesRepository.delete(file);
        }
    }
}

