package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.ScaleDTO;
import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.ScaleService;
import com.cretf.backend.product.service.StatusService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scale")
public class ScaleController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final ScaleService scaleService;

    public ScaleController(ScaleService scaleService) {
        this.scaleService = scaleService;
    }

    @PostMapping("/getAllScale")
    public Response<List<ScaleDTO>> getAllScale(@RequestBody ScaleDTO scaleDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllScale");
        Page<ScaleDTO> result = scaleService.getScaleBySearch(scaleDTO, pageable);
        return Response.ok(result);
    }
}
