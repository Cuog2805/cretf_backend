package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.CoordinatesDTO;
import com.cretf.backend.product.entity.Coordinates;
import com.cretf.backend.product.repository.CategorySharedRepository;
import com.cretf.backend.product.repository.CoodinatesRepository;
import com.cretf.backend.product.service.CoordinatesService;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesServiceImpl extends BaseJdbcServiceImpl<CoordinatesDTO, String> implements CoordinatesService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/CategoryShared";

    private final ModelMapper modelMapper;
    private final CoodinatesRepository coodinatesRepository;

    public CoordinatesServiceImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            CoodinatesRepository coodinatesRepository) {
        super(entityManager, CoordinatesDTO.class);
        this.modelMapper = modelMapper;
        this.coodinatesRepository = coodinatesRepository;
    }
    @Override
    public CoordinatesDTO findByPropertyId(String propertyId){
        Coordinates coordinates = coodinatesRepository.findByPropertyId(propertyId).orElse(new Coordinates());
        return modelMapper.map(coordinates, CoordinatesDTO.class);
    }
}
