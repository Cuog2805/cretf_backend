package com.cretf.backend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyFilesDTO {
    private String propertyFilesId;

    private String propertyId;

    private List<String> fileIds;

    private String category;
}
