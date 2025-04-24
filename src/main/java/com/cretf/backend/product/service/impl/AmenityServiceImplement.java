package com.cretf.backend.product.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.AmenityDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.repository.AmenityRepository;
import com.cretf.backend.product.service.AmenityService;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cretf.backend.utils.NativeSqlBuilder.buildSqlWithParams;

@Service
public class AmenityServiceImplement extends BaseJdbcServiceImpl<AmenityDTO, String> implements AmenityService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Amenity";

    private final ModelMapper modelMapper;
    private final AmenityRepository amenityRepository;

    public AmenityServiceImplement(
            EntityManager entityManager,
            ModelMapper modelMapper,
            AmenityRepository amenityRepository) {
        super(entityManager, AmenityDTO.class);
        this.modelMapper = modelMapper;
        this.amenityRepository = amenityRepository;
    }

    @Override
    public List<AmenityDTO> getAllAmenityBySearch(AmenityDTO amenityDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPropertyAmenity", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("propertyId", "pa");
        aliasMap.put("isGeneralInfo", "a");

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithParams(sqlSelect, amenityDTO, aliasMap);

        List<AmenityDTO> result = (List<AmenityDTO>) this.findAndAliasToBeanResultTransformerList(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, AmenityDTO.class);
        return result;
    }
}
