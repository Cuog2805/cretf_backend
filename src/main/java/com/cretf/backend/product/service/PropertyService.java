package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyDTO;

import java.util.List;

public interface PropertyService{
    public PropertyDTO create(PropertyDTO propertyDTO) throws Exception;
    public List<PropertyDTO> createMulti(List<PropertyDTO> propertyDTOS) throws Exception;
    public  List<PropertyDTO> getPropertyBySearch(PropertyDTO propertyDTOS) throws Exception;
}

