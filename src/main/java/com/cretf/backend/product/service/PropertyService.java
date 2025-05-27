package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyService{
    public PropertyDTO create(PropertyDTO propertyDTO) throws Exception;
    public PropertyDTO update(PropertyDTO propertyDTO) throws Exception;
    public boolean delete(String propertyId) throws Exception;
    public List<PropertyDTO> createMulti(List<PropertyDTO> propertyDTOS) throws Exception;
    public Page<PropertyDTO> getPropertyBySearch(PropertyDTO propertyDTOS, Pageable pageable) throws Exception;
    public Page<PropertyDTO> getPropertyFavourite(PropertyDTO propertyDTOS, Pageable pageable) throws Exception;
    public List<PropertyDTO> getPropertyByLocation(String locationId) throws Exception;
    public PropertyDTO getOneProperty(PropertyDTO propertyDTO) throws Exception;
    public boolean approve(PropertyDTO propertyDTO) throws Exception;
    public boolean repost(PropertyDTO propertyDTO) throws Exception;
    public boolean addToFavourite(PropertyDTO propertyDTO) throws Exception;
    public boolean removeToFavourite(PropertyDTO propertyDTO) throws Exception;
    public boolean lock(PropertyDTO propertyDTO) throws Exception;
    public boolean unLock(PropertyDTO propertyDTO) throws Exception;
}

