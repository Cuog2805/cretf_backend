package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyFilesDTO;

import java.util.List;

public interface PropertyFilesService {
    public List<PropertyFilesDTO> getAllByPropertyId(List<String> propertyIds) throws Exception;
}
