package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyCommentDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PropertyTypeDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PropertyCommentService;
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
    private final PropertyCommentService propertyCommentService;

    public PropertyController(
            PropertyService propertyService,
            PropertyCommentService propertyCommentService
    ) {
        this.propertyService = propertyService;
        this.propertyCommentService = propertyCommentService;
    }

    @PostMapping("/getAllProperties")
    public Response<List<PropertyDTO>> getAllProperties(@RequestBody PropertyDTO propertyDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllProperties");
        Page<PropertyDTO> result = propertyService.getPropertyBySearch(propertyDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("/getFavouriteProperties")
    public Response<List<PropertyDTO>> getFavouriteProperties(@RequestBody PropertyDTO propertyDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getFavouriteProperties");
        Page<PropertyDTO> result = propertyService.getPropertyFavourite(propertyDTO, pageable);
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

    @PostMapping("/addToFavourite")
    public Response<String> addToFavourite(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("REST request to lock addToFavourite : {}", propertyDTO.getPropertyId());
        boolean result = propertyService.addToFavourite(propertyDTO);
        if (result) {
            return Response.ok("Add succeed!");
        }
        throw new Exception("Add fail!");
    }

    @PostMapping("/removeToFavourite")
    public Response<String> removeToFavourite(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("REST request to lock removeToFavourite : {}", propertyDTO.getPropertyId());
        boolean result = propertyService.removeToFavourite(propertyDTO);
        if (result) {
            return Response.ok("Remove succeed!");
        }
        throw new Exception("Remove fail!");
    }

    @PostMapping("/approveProperty")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> approveProperty(@RequestBody PropertyDTO propertyDTO) throws Exception {
        log.debug("REST request to lock approveProperty : {}", propertyDTO.getPropertyId());
        boolean result = propertyService.approve(propertyDTO);
        if (result) {
            return Response.ok("Approve succeed!");
        }
        throw new Exception("Approve fail!");
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

    @PostMapping("/createPropertyComment")
    public Response<PropertyCommentDTO> createPropertyComment(@RequestBody PropertyCommentDTO propertyCommentDTO) throws Exception {
        log.debug("Rest request to createPropertyComment: {}", propertyCommentDTO);
        PropertyCommentDTO result = propertyCommentService.create(propertyCommentDTO);
        return Response.ok(result);
    }

    @DeleteMapping("/deletePropertyComment/{id}")
    public Response<String> deletePropertyComment(@PathVariable(value = "id", required = false) String id) throws Exception {
        log.debug("REST request to delete deletePropertyComment : {}", id);
        boolean result = propertyService.delete(id);
        if (result) {
            return Response.ok("Delete succeed!");
        }
        throw new Exception("Delete fail!");
    }
}