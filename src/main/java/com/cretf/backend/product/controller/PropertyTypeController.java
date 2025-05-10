package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyTypeDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PropertyTypeService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/propertyType")
public class PropertyTypeController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final PropertyTypeService propertyTypeService;

    public PropertyTypeController(PropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    @GetMapping("/getAllPropertyType")
    public Response<List<PropertyTypeDTO>> getAllPropertyType() throws Exception {
        log.debug("Rest request to getAllPropertyType");
        List<PropertyTypeDTO> result = propertyTypeService.getAllPropertyType();
        return Response.ok(result);
    }
}
