package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyPriceHistoryDTO;
import com.cretf.backend.product.entity.PropertyPriceHistory;
import com.cretf.backend.product.repository.PropertyPriceHistoryRepository;
import com.cretf.backend.product.service.PropertyPriceHistoryService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyPriceHistoryServiceImpl extends BaseJdbcServiceImpl<PropertyPriceHistoryDTO, String> implements PropertyPriceHistoryService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Property";

    private final PropertyPriceHistoryRepository propertyPriceHistoryRepository;
    private final ModelMapper modelMapper;

    public PropertyPriceHistoryServiceImpl(
            EntityManager entityManager,
            PropertyPriceHistoryRepository propertyPriceHistoryRepository,
            ModelMapper modelMapper) {
        super(entityManager, PropertyPriceHistoryDTO.class);
        this.propertyPriceHistoryRepository = propertyPriceHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PropertyPriceHistoryDTO> findByPropertyIds(List<String> propertyIds) throws Exception {
        List<PropertyPriceHistory> propertyPriceHistorys = propertyPriceHistoryRepository.findByPropertyIds(propertyIds);

        List<PropertyPriceHistoryDTO> propertyPriceHistoryDTOs = propertyPriceHistorys.stream().map(item -> {
            return modelMapper.map(item, PropertyPriceHistoryDTO.class);
        }).collect(Collectors.toList());

        return propertyPriceHistoryDTOs;
    }

    @Override
    public List<PropertyPriceHistoryDTO> findPriceNewestByPropertyIds(List<String> propertyIds) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPropertyPriceNewest", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        params.put("propertyIds", propertyIds);

        List<PropertyPriceHistoryDTO> propertyPriceHistoryDTOs = (List<PropertyPriceHistoryDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, PropertyPriceHistoryDTO.class);

        return propertyPriceHistoryDTOs;
    }
}
