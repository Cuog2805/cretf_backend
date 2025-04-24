package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.AmenityDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PropertyFilesDTO;
import com.cretf.backend.product.dto.PropertyPriceHistoryDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.entity.PropertyFiles;
import com.cretf.backend.product.repository.PropertyPriceHistoryRepository;
import com.cretf.backend.product.repository.PropertyRepository;
import com.cretf.backend.product.service.AmenityService;
import com.cretf.backend.product.service.PropertyFilesService;
import com.cretf.backend.product.service.PropertyPriceHistoryService;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImplement extends BaseJdbcServiceImpl<PropertyDTO, String> implements PropertyService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Property";

    private final ModelMapper modelMapper;
    private final PropertyRepository propertyRepository;
    private final AmenityService amenityService;
    private final PropertyFilesService propertyFilesService;
    private final PropertyPriceHistoryService propertyPriceHistoryService;

    public PropertyServiceImplement(
            EntityManager entityManager,
            ModelMapper modelMapper,
            PropertyRepository propertyRepository,
            AmenityService amenityService,
            PropertyFilesService propertyFilesService,
            PropertyPriceHistoryService propertyPriceHistoryService
    ) {
        super(entityManager, PropertyDTO.class);
        this.modelMapper = modelMapper;
        this.propertyRepository = propertyRepository;
        this.amenityService = amenityService;
        this.propertyFilesService = propertyFilesService;
        this.propertyPriceHistoryService = propertyPriceHistoryService;
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
    public Page<PropertyDTO> getPropertyBySearch(PropertyDTO propertyDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllProperty", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllProperty", FILE_EXTENSION, FILE_PATH_NAME);
        //param
        Map<String, Object> params = new HashMap<>();
//        Map<String, String> aliasMap = new HashMap<>();
//        aliasMap.put("propertyId", "pa");
//        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithParams(sqlSelect, propertyDTO, aliasMap);

        List<PropertyDTO> propertyDTOs = (List<PropertyDTO>) this.findAndAliasToBeanResultTransformer(sqlSelect, params, pageable, PropertyDTO.class);
        Long total = this.countByNativeQuery(sqlCount, params);

        //Gán phần thông tin chung
        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setIsGeneralInfo(true);
        List<AmenityDTO> amenityDTOS = amenityService.getAllAmenityBySearch(amenityDTO);
        Map<String, List<AmenityDTO>> amenityGrouped = amenityDTOS.stream()
                .filter(a -> a.getPropertyId() != null)
                .collect(Collectors.groupingBy(AmenityDTO::getPropertyId));
        for (PropertyDTO property : propertyDTOs) {
            String propertyId = property.getPropertyId();
            List<AmenityDTO> amenities = amenityGrouped.getOrDefault(propertyId, new ArrayList<>());
            property.setAmenityDTOs(amenities);
        }

        List<String> propertyIds = propertyDTOs.stream().map(PropertyDTO::getPropertyId).toList();
        //Gán file
        List<PropertyFilesDTO> propertyFilesDTOs = propertyFilesService.getAllByPropertyId(propertyIds);
        Map<String, List<PropertyFilesDTO>> propertyFilesGrouped = propertyFilesDTOs.stream()
                .filter(a -> a.getPropertyId() != null)
                .collect(Collectors.groupingBy(PropertyFilesDTO::getPropertyId));
        for (PropertyDTO property : propertyDTOs) {
            String propertyId = property.getPropertyId();
            List<PropertyFilesDTO> propertyFiles = propertyFilesGrouped.getOrDefault(propertyId, new ArrayList<>());
            property.setPropertyFilesDTOs(propertyFiles);
        }
        //gán price
        List<PropertyPriceHistoryDTO> propertyPriceNewestDTOs = propertyPriceHistoryService.findPriceNewestByPropertyIds(propertyIds);
        Map<String, PropertyPriceHistoryDTO> propertyPriceNewestMap = propertyPriceNewestDTOs.stream()
                .collect(Collectors.toMap(PropertyPriceHistoryDTO::getPropertyId, Function.identity()));

        propertyDTOs.forEach(item ->
                item.setPropertyPriceNewest(propertyPriceNewestMap.get(item.getPropertyId()))
        );

        return new PageImpl<>(propertyDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }

    @Override
    public PropertyDTO getOneProperty(PropertyDTO propertyDTO) throws Exception {
        Optional<Property> entity = propertyRepository.findById(propertyDTO.getPropertyId());
        if(entity.isPresent()){
            Property property = entity.get();
            PropertyDTO result = modelMapper.map(property, PropertyDTO.class);

            AmenityDTO amenityDTO = new AmenityDTO();
            amenityDTO.setPropertyId(result.getPropertyId());
            result.setAmenityDTOs(amenityService.getAllAmenityBySearch(amenityDTO));

            result.setPropertyFilesDTOs(propertyFilesService.getAllByPropertyId(Collections.singletonList(property.getPropertyId())));
            result.setPropertyPriceNewest(propertyPriceHistoryService.findPriceNewestByPropertyIds(Collections.singletonList(property.getPropertyId())).getFirst());
            result.setPropertyPriceHistoryDTOs(propertyPriceHistoryService.findByPropertyIds(Collections.singletonList(property.getPropertyId())));

            return result;
        }
        throw new Exception("Property not found with ID: " + propertyDTO.getPropertyId());
    }

}
