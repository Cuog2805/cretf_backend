package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyService{
    public PropertyDTO create(PropertyDTO propertyDTO) throws Exception;
    public List<PropertyDTO> createMulti(List<PropertyDTO> propertyDTOS) throws Exception;
    public Page<PropertyDTO> getPropertyBySearch(PropertyDTO propertyDTOS, Pageable pageable) throws Exception;
    public PropertyDTO getOneProperty(PropertyDTO propertyDTO) throws Exception;
}

