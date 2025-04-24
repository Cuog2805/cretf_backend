package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.AmenityDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.AmenityService;
import com.cretf.backend.product.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amenity")
public class AmenityController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

//    @PostMapping("/getAllAmenities")
//    public Page<AmenityDTO> getAllAmenities(@RequestBody AmenityDTO amenityDTO, @ParameterObject Pageable pageable) throws Exception {
//        log.debug("Rest request to getAllProperties");
//        Page<AmenityDTO> result = amenityService.getAmenityBySearch(amenityDTO, pageable);
//        return result;
//    }
}
