package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.CategorySharedDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.CategoryShared;

import java.util.List;

public interface CategorySharedService {
    public CategorySharedDTO create(CategorySharedDTO categorySharedDTO) throws Exception;
    public List<CategorySharedDTO> createMulti(List<CategorySharedDTO> categorySharedDTOS) throws Exception;
    public List<CategorySharedDTO> getCategorySharedBySearch(CategorySharedDTO categorySharedDTO) throws Exception;
}
