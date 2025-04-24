package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.CategorySharedDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.CategoryShared;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.repository.CategorySharedRepository;
import com.cretf.backend.product.service.CategorySharedService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategorySharedServiceImplement extends BaseJdbcServiceImpl<CategorySharedDTO, String> implements CategorySharedService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/CategoryShared";

    private final ModelMapper modelMapper;
    private final CategorySharedRepository categorySharedRepository;

    public CategorySharedServiceImplement(EntityManager entityManager, ModelMapper modelMapper, CategorySharedRepository categorySharedRepository) {
        super(entityManager, CategorySharedDTO.class);
        this.modelMapper = modelMapper;
        this.categorySharedRepository = categorySharedRepository;
    }

    @Override
    public CategorySharedDTO create(CategorySharedDTO categorySharedDTO) throws Exception {
        CategoryShared categoryShared = modelMapper.map(categorySharedDTO, CategoryShared.class);
        categoryShared = categorySharedRepository.save(categoryShared);
        BeanUtils.copyProperties(categoryShared, categorySharedDTO);
        return categorySharedDTO;
    }

    @Override
    public List<CategorySharedDTO> createMulti(List<CategorySharedDTO> categorySharedDTOS) throws Exception {
        List<CategoryShared> categoryShareds = categorySharedDTOS.stream()
                .map(item -> modelMapper.map(item, CategoryShared.class))
                .collect(Collectors.toList());
        categoryShareds = categorySharedRepository.saveAll(categoryShareds);
        return categoryShareds.stream()
                .map(entity -> modelMapper.map(entity, CategorySharedDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorySharedDTO> getCategorySharedBySearch(CategorySharedDTO categorySharedDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllCategoryShared", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        //String a = categorySharedDTO.getCategorySharedId();
        //categorySharedDTO.set
        return (List<CategorySharedDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, CategorySharedDTO.class);
    }

}
