package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.AmenityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AmenityService {
    public List<AmenityDTO> getAllAmenityBySearch(AmenityDTO amenityDTO) throws Exception;
}
