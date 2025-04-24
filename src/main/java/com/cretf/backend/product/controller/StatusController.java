package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.entity.Status;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.product.service.StatusService;
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
@RequestMapping("/status")
public class StatusController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping("/getAllStatus")
    public Page<StatusDTO> getAllStatus(@RequestBody StatusDTO statusDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllStatus");
        Page<StatusDTO> result = statusService.getStatusBySearch(statusDTO, pageable);
        return result;
    }
}
