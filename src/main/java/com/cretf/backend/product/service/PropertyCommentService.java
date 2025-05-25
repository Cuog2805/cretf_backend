package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.LocationDTO;
import com.cretf.backend.product.dto.PropertyCommentDTO;

import java.util.List;

public interface PropertyCommentService {
    public List<PropertyCommentDTO> getPropertyCommentByPropertyId(String propertyId) throws Exception;
    public PropertyCommentDTO create(PropertyCommentDTO propertyCommentDTO) throws Exception;
    public boolean delete(String propertyCommentId) throws Exception;
}
