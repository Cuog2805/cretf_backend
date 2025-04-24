package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyFilesDTO;
import com.cretf.backend.product.entity.PropertyFiles;
import com.cretf.backend.product.repository.PropertyFilesRepository;
import com.cretf.backend.product.service.PropertyFilesService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class PropertyFilesServiceImplement extends BaseJdbcServiceImpl<PropertyFilesDTO, String> implements PropertyFilesService {
    public final PropertyFilesRepository propertyFilesRepository;
    public final ModelMapper modelMapper;

    public PropertyFilesServiceImplement(
            EntityManager entityManager,
            PropertyFilesRepository propertyFilesRepository,
            ModelMapper modelMapper) {
        super(entityManager, PropertyFilesDTO.class);
        this.propertyFilesRepository = propertyFilesRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<PropertyFilesDTO> getAllByPropertyId(List<String> propertyIds) throws Exception {
        List<PropertyFiles> propertyFiles = propertyFilesRepository.findByPropertyIds(propertyIds);

        List<PropertyFilesDTO> propertyFilesDTOs =  propertyFiles.stream().map(item -> {
            return modelMapper.map(item, PropertyFilesDTO.class);
        }).collect(Collectors.toList());

        return propertyFilesDTOs;
    }
}
