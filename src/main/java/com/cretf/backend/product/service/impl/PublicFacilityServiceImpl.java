package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.CoordinatesDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PublicFacilityDTO;
import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.entity.Coordinates;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.entity.PropertyNearbyPlace;
import com.cretf.backend.product.entity.PublicFacility;
import com.cretf.backend.product.repository.CoodinatesRepository;
import com.cretf.backend.product.repository.PropertyNearbyPlaceRepository;
import com.cretf.backend.product.repository.PublicFacilityRepository;
import com.cretf.backend.product.service.CoordinatesService;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.product.service.PublicFacilityService;
import com.cretf.backend.security.SecurityUtils;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PublicFacilityServiceImpl extends BaseJdbcServiceImpl<PublicFacilityDTO, String> implements PublicFacilityService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/PublicFacility";

    private final ModelMapper modelMapper;
    private final PublicFacilityRepository publicFacilityRepository;
    private final CoodinatesRepository coodinatesRepository;
    private final CoordinatesService coordinatesService;
    private final PropertyService propertyService;
    private final PropertyNearbyPlaceRepository propertyNearbyPlaceRepository;

    public PublicFacilityServiceImpl(
            EntityManager entityManager, ModelMapper modelMapper,
            PublicFacilityRepository publicFacilityRepository,
            CoordinatesService coordinatesService,
            CoodinatesRepository coodinatesRepository,
            @Lazy PropertyService propertyService,
            PropertyNearbyPlaceRepository propertyNearbyPlaceRepository
    ) {
        super(entityManager, PublicFacilityDTO.class);
        this.modelMapper = modelMapper;
        this.publicFacilityRepository = publicFacilityRepository;
        this.coodinatesRepository = coodinatesRepository;
        this.coordinatesService = coordinatesService;
        this.propertyService = propertyService;
        this.propertyNearbyPlaceRepository = propertyNearbyPlaceRepository;
    }

    @Override
    public Page<PublicFacilityDTO> getPublicFacilityBySearch(PublicFacilityDTO publicFacilityDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPublicFacility", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllPublicFacility", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, NativeSqlBuilder.ColumnInfo> columnInfoMap = NativeSqlBuilder.createColumnInfoMap();

        NativeSqlBuilder.addColumnInfo(columnInfoMap, "locationIds", "pf.LocationId", NativeSqlBuilder.ComparisonType.IN);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithColumnInfo(sqlSelect, publicFacilityDTO, columnInfoMap);
        List<PublicFacilityDTO> publicFacilityDTOs = (List<PublicFacilityDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, PublicFacilityDTO.class);

        List<String> publicFacilityIds = publicFacilityDTOs.stream().map(PublicFacilityDTO::getPublicFacilityId).toList();
        List<CoordinatesDTO> coordinatesDTOs = coordinatesService.findByPropertyIds(publicFacilityIds);
        Map<String, CoordinatesDTO> publicFacilityCoordinateMap = coordinatesDTOs.stream().collect(Collectors.toMap(
                CoordinatesDTO::getPropertyId, Function.identity()
        ));

        publicFacilityDTOs.stream().forEach(item -> {
            item.setCoordinatesDTO(publicFacilityCoordinateMap.get(item.getPublicFacilityId()));
        });

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithColumnInfo(sqlCount, publicFacilityDTO, columnInfoMap);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        return new PageImpl<>(publicFacilityDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }

    @Override
    public List<PublicFacilityDTO> findByPropertyId(String propertyId) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPublicFacilityByPropertyId", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        params.put("propertyId", propertyId);

        List<PublicFacilityDTO> result = (List<PublicFacilityDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, PublicFacilityDTO.class);
        result.stream().forEach(item -> {
            item.setCoordinatesDTO(coordinatesService.findByPropertyId(item.getPublicFacilityId()));
        });
        return result;
    }

    @Override
    public boolean lock(PublicFacilityDTO publicFacilityDTO) throws Exception {
        Optional<PublicFacility> publicFacility = publicFacilityRepository.findById(publicFacilityDTO.getPublicFacilityId());
        if (publicFacility.isPresent()) {
            PublicFacility publicFacilityExisting = publicFacility.get();
            publicFacilityExisting.setIsDeleted(1);
            publicFacilityRepository.save(publicFacilityExisting);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlock(PublicFacilityDTO publicFacilityDTO) throws Exception {
        Optional<PublicFacility> publicFacility = publicFacilityRepository.findById(publicFacilityDTO.getPublicFacilityId());
        if (publicFacility.isPresent()) {
            PublicFacility publicFacilityExisting = publicFacility.get();
            publicFacilityExisting.setIsDeleted(0);
            publicFacilityRepository.save(publicFacilityExisting);
            return true;
        }
        return false;
    }

    @Override
    public PublicFacilityDTO create(PublicFacilityDTO publicFacilityDTO) throws Exception {
        PublicFacility publicFacility;
        boolean isUpdate = publicFacilityDTO.getPublicFacilityId() != null;

        if (isUpdate) {
            // Update existing
            publicFacility = publicFacilityRepository.findById(publicFacilityDTO.getPublicFacilityId())
                    .orElseThrow(() -> new Exception("PublicFacility not found"));
            publicFacility.setName(publicFacilityDTO.getName());
            publicFacility.setLocationId(publicFacilityDTO.getLocationId());
            publicFacility.setDateModified(new Date());
            publicFacility.setModifier(SecurityUtils.getCurrentUserLogin().get());
        } else {
            // Create new
            publicFacility = modelMapper.map(publicFacilityDTO, PublicFacility.class);
            publicFacility.setDateCreated(new Date());
        }

        publicFacilityRepository.save(publicFacility);
        String publicFacilityId = publicFacility.getPublicFacilityId();

        // Handle coordinates
        if (publicFacilityDTO.getCoordinatesDTO() != null) {
            CoordinatesDTO coordinatesDTO = publicFacilityDTO.getCoordinatesDTO();
            coordinatesDTO.setPropertyId(publicFacilityId);

            if (isUpdate) {
                // Update or create coordinates
                Optional<Coordinates> existingCoords = coodinatesRepository.findByPropertyId(publicFacilityId);
                if (existingCoords.isPresent()) {
                    existingCoords.get().setLatitude(coordinatesDTO.getLatitude());
                    existingCoords.get().setLongitude(coordinatesDTO.getLongitude());
                    coodinatesRepository.save(existingCoords.get());
                } else {
                    Coordinates coordinates = modelMapper.map(coordinatesDTO, Coordinates.class);
                    coodinatesRepository.save(coordinates);
                }
                List<PropertyNearbyPlace> propertyNearbyPlaceOld = propertyNearbyPlaceRepository.findByPublicFacilityId(publicFacilityId);
                propertyNearbyPlaceRepository.deleteAll(propertyNearbyPlaceOld);
            } else {
                // Create new coordinates
                Coordinates coordinates = modelMapper.map(coordinatesDTO, Coordinates.class);
                coodinatesRepository.save(coordinates);
            }

            // Recalculate distances
            List<PropertyDTO> propertyDTOs = propertyService.getPropertyByLocation(publicFacility.getLocationId());
            propertyDTOs.forEach(item -> {
                CoordinatesDTO propertyCoordinatesDTO = coordinatesService.findByPropertyId(item.getPropertyId());
                if (propertyCoordinatesDTO != null) {
                    PropertyNearbyPlace propertyNearbyPlace = new PropertyNearbyPlace();
                    propertyNearbyPlace.setPropertyId(item.getPropertyId());
                    propertyNearbyPlace.setPublicFacilityId(publicFacilityId);
                    propertyNearbyPlace.setDistance(calculateDistance(propertyCoordinatesDTO, coordinatesDTO));
                    propertyNearbyPlaceRepository.save(propertyNearbyPlace);
                }
            });
        }

        PublicFacilityDTO result = modelMapper.map(publicFacility, PublicFacilityDTO.class);
        result.setCoordinatesDTO(coordinatesService.findByPropertyId(publicFacilityId));

        return result;
    }

    private static final double EARTH_RADIUS_M = 6371000.0;

    public static double calculateDistance(CoordinatesDTO coordinatesDTO1, CoordinatesDTO coordinatesDTO2) {
        if (coordinatesDTO1 == null || coordinatesDTO2 == null ||
                coordinatesDTO1.getLatitude() == null || coordinatesDTO1.getLongitude() == null ||
                coordinatesDTO2.getLatitude() == null || coordinatesDTO2.getLongitude() == null) {
            return 0;
        }
        // Đổi độ sang radian
        double lat1Rad = Math.toRadians(coordinatesDTO1.getLatitude());
        double lon1Rad = Math.toRadians(coordinatesDTO1.getLongitude());
        double lat2Rad = Math.toRadians(coordinatesDTO2.getLatitude());
        double lon2Rad = Math.toRadians(coordinatesDTO2.getLongitude());

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Công thức Haversine
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_M * c;
    }
}
