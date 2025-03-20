package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/getAllProperties")
    public List<PropertyDTO> getAllProperties(PropertyDTO propertyDTO) throws Exception {
        log.debug("Rest request to getAllProperties");
        List<PropertyDTO> result = propertyService.getPropertyBySearch(propertyDTO);
        return result;
    }

    @PostMapping("/createProperty")
    public PropertyDTO createProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("Rest request to createProperty: {}", propertyDTO);
        PropertyDTO result = propertyService.create(propertyDTO);
        return result;
    }

    @PostMapping("createMultiProperty")
    public List<PropertyDTO> createMultiProperty(@RequestBody List<PropertyDTO> propertyDTOS) throws Exception {
        log.debug("Rest request to createMultiProperty: {}", propertyDTOS);
        List<PropertyDTO> result = propertyService.createMulti(propertyDTOS);
        return result;
    }
}