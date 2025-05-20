package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.CoordinatesDTO;

public interface CoordinatesService {
    public CoordinatesDTO findByPropertyId(String propertyId);
}
