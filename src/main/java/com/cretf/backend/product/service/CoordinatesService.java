package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.CoordinatesDTO;

import java.util.List;

public interface CoordinatesService {
    public CoordinatesDTO findByPropertyId(String propertyId);
    public List<CoordinatesDTO> findByPropertyIds(List<String> propertyIds);
}
