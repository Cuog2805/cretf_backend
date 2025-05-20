package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PublicFacilityDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PublicFacilityService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publicFacility")
public class PublicFacilityController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final PublicFacilityService publicFacilityService;

    public PublicFacilityController(PublicFacilityService publicFacilityService) {
        this.publicFacilityService = publicFacilityService;
    }

    @PostMapping("/getAllPublicFacility")
    public Response<List<PublicFacilityDTO>> getAllPublicFacility(@RequestBody PublicFacilityDTO publicFacilityDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllPublicFacility");
        Page<PublicFacilityDTO> result = publicFacilityService.getPublicFacilityBySearch(publicFacilityDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("/createPublicFacility")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<PublicFacilityDTO> createPublicFacility(@RequestBody PublicFacilityDTO publicFacilityDTO) throws Exception {
        log.debug("Rest request to createPublicFacility: {}", publicFacilityDTO);
        PublicFacilityDTO result = publicFacilityService.create(publicFacilityDTO);
        return Response.ok(result);
    }

    @PostMapping("/lockPublicFacility")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> lockPublicFacility(@RequestBody PublicFacilityDTO publicFacilityDTO) throws Exception {
        log.debug("REST request to lockPublicFacility : {}", publicFacilityDTO.getPublicFacilityId());
        boolean result = publicFacilityService.lock(publicFacilityDTO);
        if (result) {
            return Response.ok("Lock succeed!");
        }
        throw new Exception("Lock fail!");
    }

    @PostMapping("/unLockPublicFacility")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> unLockPublicFacility(@RequestBody PublicFacilityDTO publicFacilityDTO) throws Exception {
        log.debug("REST request to unLockPublicFacility : {}", publicFacilityDTO.getPublicFacilityId());
        boolean result = publicFacilityService.unlock(publicFacilityDTO);
        if (result) {
            return Response.ok("Unlock succeed!");
        }
        throw new Exception("Unlock fail!");
    }
}
