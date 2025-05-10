package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.LocationDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.LocationService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/getAllLocation")
    public Response<List<LocationDTO>> getAllLocation() throws Exception {
        log.debug("Rest request to getAllProperties");
        List<LocationDTO> result = locationService.getALlLocation();
        return Response.ok(result);
    }
}
