package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PropertyTypeDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.product.service.PropertyTypeService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/getAllProperties")
    public Response<List<PropertyDTO>> getAllProperties(@RequestBody PropertyDTO propertyDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllProperties");
        Page<PropertyDTO> result = propertyService.getPropertyBySearch(propertyDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("getOneDetailProperty")
    public Response<PropertyDTO> getOneDetailProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("Rest request to getOneDetailProperty: {}", propertyDTO);
        PropertyDTO result = propertyService.getOneProperty(propertyDTO);
        return Response.ok(result);
    }

    @PostMapping("/createProperty")
    public Response<PropertyDTO> createProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("Rest request to createProperty: {}", propertyDTO);
        PropertyDTO result = propertyService.create(propertyDTO);
        return Response.ok(result);
    }

    @PostMapping("/updateProperty")
    public Response<PropertyDTO> updateProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("Rest request to updateProperty: {}", propertyDTO);
        PropertyDTO result = propertyService.update(propertyDTO);
        return Response.ok(result);
    }

    @PostMapping("/createMultiProperty")
    public List<PropertyDTO> createMultiProperty(@RequestBody List<PropertyDTO> propertyDTOS) throws Exception {
        log.debug("Rest request to createMultiProperty: {}", propertyDTOS);
        List<PropertyDTO> result = propertyService.createMulti(propertyDTOS);
        return result;
    }

    @DeleteMapping("/deleteProperty/{id}")
    public Response<String> deleteProperty(@PathVariable(value = "id", required = false) String id) throws Exception {
        log.debug("REST request to delete deleteProperty : {}", id);
        boolean result = propertyService.delete(id);
        if (result) {
            return Response.ok("Delete succeed!");
        }
        throw new Exception("Delete fail!");
    }

    @PostMapping("/lockProperty")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> lockProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("REST request to lock lockProperty : {}", propertyDTO.getPropertyId());
        boolean result = propertyService.lock(propertyDTO);
        if (result) {
            return Response.ok("Lock succeed!");
        }
        throw new Exception("Lock fail!");
    }

    @PostMapping("/unLockProperty")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> unLockProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("REST request to lock unLockProperty : {}", propertyDTO.getPropertyId());
        boolean result = propertyService.unLock(propertyDTO);
        if (result) {
            return Response.ok("Unlock succeed!");
        }
        throw new Exception("Unlock fail!");
    }
}