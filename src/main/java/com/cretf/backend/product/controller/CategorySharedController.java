package com.cretf.backend.product.controller;

import com.cretf.backend.product.dto.CategorySharedDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.CategorySharedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categoryShared")
public class CategorySharedController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final CategorySharedService categorySharedService;

    public CategorySharedController(CategorySharedService categorySharedService) {
        this.categorySharedService = categorySharedService;
    }

    @PostMapping("/createCategoryShared")
    public CategorySharedDTO createCategoryShared(@RequestBody CategorySharedDTO categorySharedDTO) throws Exception {
        log.debug("Rest request to createCategoryShared: {}", categorySharedDTO);
        CategorySharedDTO result = categorySharedService.create(categorySharedDTO);
        return result;
    }

    @PostMapping("/createMultiCategoryShared")
    public List<CategorySharedDTO> createMultiCategoryShared(@RequestBody List<CategorySharedDTO> categorySharedDTOS) throws Exception {
        log.debug("Rest request to createMultiCategoryShared: {}", categorySharedDTOS);
        List<CategorySharedDTO> result = categorySharedService.createMulti(categorySharedDTOS);
        return result;
    }

    @PostMapping("/getAllCategoryShared")
    public List<CategorySharedDTO> getAllCategoryShared(@RequestBody CategorySharedDTO categorySharedDTO) throws Exception {
        log.debug("Rest request to getAllCategoryShared: {}", categorySharedDTO);
        List<CategorySharedDTO> result = categorySharedService.getCategorySharedBySearch(categorySharedDTO);
        return result;
    }
}
