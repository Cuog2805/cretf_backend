package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.LocationDTO;
import com.cretf.backend.product.entity.Location;
import com.cretf.backend.product.repository.LocationRepository;
import com.cretf.backend.product.repository.PropertyRepository;
import com.cretf.backend.product.service.LocationService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl extends BaseJdbcServiceImpl<Location, String> implements LocationService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Location";

    private final ModelMapper modelMapper;
    private final LocationRepository locationRepository;

    public LocationServiceImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            LocationRepository locationRepository
    ) {
        super(entityManager, Location.class);
        this.modelMapper = modelMapper;
        this.locationRepository = locationRepository;
    }


    @Override
    public List<LocationDTO> getALlLocation() throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllLocation", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        List<LocationDTO> locationDTOs = (List<LocationDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, LocationDTO.class);

        return locationDTOs;
    }
}
