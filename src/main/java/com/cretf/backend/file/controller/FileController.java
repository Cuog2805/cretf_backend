package com.cretf.backend.file.controller;

import com.cretf.backend.file.fileservice.FileUploadService;
import com.cretf.backend.file.dto.FilesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileUploadService fileService;

    public FileController(FileUploadService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public ResponseEntity<FilesDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FilesDTO uploadedFile = fileService.uploadFile(file);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/uploadMulti", headers = "content-type=multipart/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FilesDTO>> uploadFileMulti(@RequestPart("files") MultipartFile[] files) {
        try {
            List<FilesDTO> uploadedFile = fileService.uploadFileMulti(files);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FilesDTO> getFileInfo(@PathVariable String fileId) {
        return fileService.getFileById(fileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{fileId}/url")
    public ResponseEntity<Map<String, String>> getFileUrl(@PathVariable String fileId) {
        try {
            Optional<FilesDTO> fileOptional = fileService.getFileById(fileId);
            if (fileOptional.isPresent()) {
                String presignedUrl = fileService.generatePresignedUrl(fileOptional.get().getPath());
                Map<String, String> response = new HashMap<>();
                response.put("url", presignedUrl);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}