package com.cretf.backend.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilesDTO {
    private String fileId;

    private String name;

    private String path;

    private String type;
}
