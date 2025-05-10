package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyTypeDTO;

import java.util.List;

public interface PropertyTypeService {
    List<PropertyTypeDTO> getAllPropertyType() throws Exception;
}
