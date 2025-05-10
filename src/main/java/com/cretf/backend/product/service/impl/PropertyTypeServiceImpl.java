package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyTypeDTO;
import com.cretf.backend.product.entity.PropertyType;
import com.cretf.backend.product.repository.PropertyRepository;
import com.cretf.backend.product.repository.PropertyTypeRepository;
import com.cretf.backend.product.service.PropertyTypeService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyTypeServiceImpl extends BaseJdbcServiceImpl<PropertyTypeDTO, String> implements PropertyTypeService {
    private final ModelMapper modelMapper;
    private final PropertyTypeRepository propertyTypeRepository;

    public PropertyTypeServiceImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            PropertyTypeRepository propertyTypeRepository
    ) {
        super(entityManager, PropertyTypeDTO.class);
        this.modelMapper = modelMapper;
        this.propertyTypeRepository = propertyTypeRepository;
    }


    @Override
    public List<PropertyTypeDTO> getAllPropertyType() throws Exception {
        List<PropertyType> propertyTypes = propertyTypeRepository.findAll();

        List<PropertyTypeDTO> propertyTypeDTOs = propertyTypes.stream().map(item -> {
            return modelMapper.map(item, PropertyTypeDTO.class);
        }).toList();

        return propertyTypeDTOs;
    }
}
