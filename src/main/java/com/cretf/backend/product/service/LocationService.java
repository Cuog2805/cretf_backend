package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getALlLocation() throws Exception;
}
