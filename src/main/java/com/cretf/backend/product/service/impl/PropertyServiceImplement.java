package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.repository.PropertyRepository;
import com.cretf.backend.product.service.PropertyService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImplement extends BaseJdbcServiceImpl<PropertyDTO, String> implements PropertyService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Property";

    private final ModelMapper modelMapper;
    private final PropertyRepository propertyRepository;

    public PropertyServiceImplement(
            EntityManager entityManager,
            ModelMapper modelMapper,
            PropertyRepository propertyRepository) {
        super(entityManager, PropertyDTO.class);
        this.modelMapper = modelMapper;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyDTO create(PropertyDTO propertyDTO) throws Exception {
        Property property = modelMapper.map(propertyDTO, Property.class);
        property = propertyRepository.save(property);
        BeanUtils.copyProperties(property, propertyDTO);
        return propertyDTO;
    }

    @Override
    public List<PropertyDTO> createMulti(List<PropertyDTO> propertyDTOS) throws Exception {
        List<Property> properties = propertyDTOS.stream()
                .map(propertyDTO -> modelMapper.map(propertyDTO, Property.class))
                .collect(Collectors.toList());

        propertyRepository.saveAll(properties);

        return properties.stream().map(property -> {
            PropertyDTO propertyDTO = new PropertyDTO();
            BeanUtils.copyProperties(property, propertyDTO);
            return propertyDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PropertyDTO> getPropertyBySearch(PropertyDTO propertyDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllProperty", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        //params.put("bdQdPhancongChucdanhId", bdQdPhancongChucdanhId);
        return (List<PropertyDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, PropertyDTO.class);
    }

}
