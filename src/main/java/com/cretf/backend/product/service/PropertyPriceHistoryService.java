package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyPriceHistoryDTO;

import java.util.List;

public interface PropertyPriceHistoryService {
    List<PropertyPriceHistoryDTO> findByPropertyIds(List<String> propertyIds) throws Exception;
    List<PropertyPriceHistoryDTO> findPriceNewestByPropertyIds(List<String> propertyIds) throws Exception;
}
