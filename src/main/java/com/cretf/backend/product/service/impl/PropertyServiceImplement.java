package com.cretf.backend.product.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.*;
import com.cretf.backend.product.entity.*;
import com.cretf.backend.product.repository.*;
import com.cretf.backend.product.service.*;
import com.cretf.backend.security.SecurityUtils;
import com.cretf.backend.users.dto.DepositDTO;
import com.cretf.backend.users.entity.Deposit;
import com.cretf.backend.users.repository.DepositsRepository;
import com.cretf.backend.users.service.DepositService;
import com.cretf.backend.users.service.impl.UserService;
import com.cretf.backend.utils.NativeSqlBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImplement extends BaseJdbcServiceImpl<PropertyDTO, String> implements PropertyService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Property";

    private final ModelMapper modelMapper;
    private final PropertyRepository propertyRepository;
    private final PropertyAmenityRepository propertyAmenityRepository;
    private final PropertyFilesRepository propertyFilesRepository;
    private final PropertyPriceHistoryRepository propertyPriceHistoryRepository;
    private final AmenityService amenityService;
    private final PropertyFilesService propertyFilesService;
    private final PropertyPriceHistoryService propertyPriceHistoryService;
    private final DepositService depositService;
    private final DepositsRepository depositsRepository;
    private final UserService userService;
    private final CoordinatesService coordinatesService;
    private final CoodinatesRepository coodinatesRepository;
    private final PublicFacilityService publicFacilityService;
    private final UserFavouriteRepository userFavouriteRepository;
    private final PropertyCommentService propertyCommentService;
    private final PropertyCommentRepository propertyCommentRepository;
    private final ApprovalHistoryService approvalHistoryService;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final StatusRepository statusRepository;

    public PropertyServiceImplement(
            EntityManager entityManager,
            ModelMapper modelMapper,
            PropertyRepository propertyRepository,
            PropertyAmenityRepository propertyAmenityRepository,
            PropertyFilesRepository propertyFilesRepository,
            PropertyPriceHistoryRepository propertyPriceHistoryRepository,
            AmenityService amenityService,
            PropertyFilesService propertyFilesService,
            PropertyPriceHistoryService propertyPriceHistoryService,
            DepositService depositService,
            DepositsRepository depositsRepository,
            UserService userService,
            CoordinatesService coordinatesService,
            CoodinatesRepository coodinatesRepository,
            PublicFacilityService publicFacilityService,
            UserFavouriteRepository userFavouriteRepository,
            PropertyCommentService propertyCommentService,
            PropertyCommentRepository propertyCommentRepository,
            ApprovalHistoryService approvalHistoryService,
            ApprovalHistoryRepository approvalHistoryRepository,
            StatusRepository statusRepository
    ) {
        super(entityManager, PropertyDTO.class);
        this.modelMapper = modelMapper;
        this.propertyRepository = propertyRepository;
        this.propertyAmenityRepository = propertyAmenityRepository;
        this.propertyFilesRepository = propertyFilesRepository;
        this.propertyPriceHistoryRepository = propertyPriceHistoryRepository;
        this.amenityService = amenityService;
        this.propertyFilesService = propertyFilesService;
        this.propertyPriceHistoryService = propertyPriceHistoryService;
        this.depositService = depositService;
        this.depositsRepository = depositsRepository;
        this.userService = userService;
        this.coordinatesService = coordinatesService;
        this.coodinatesRepository = coodinatesRepository;
        this.publicFacilityService = publicFacilityService;
        this.userFavouriteRepository = userFavouriteRepository;
        this.propertyCommentService = propertyCommentService;
        this.propertyCommentRepository = propertyCommentRepository;
        this.approvalHistoryService = approvalHistoryService;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.statusRepository = statusRepository;
    }

    private String generatePropertyCode(PropertyDTO propertyDTO) {
        StringBuilder codeBuilder = new StringBuilder();
        String prefix = "PROP";
        codeBuilder.append(prefix).append("-");

        String locationCode = "XX";
        if (propertyDTO.getLocationId() != null && !propertyDTO.getLocationId().isEmpty()) {
            if (propertyDTO.getLocationId().length() >= 2) {
                locationCode = propertyDTO.getLocationId().substring(0, 2).toUpperCase();
            } else {
                locationCode = propertyDTO.getLocationId().toUpperCase();
            }
        }
        codeBuilder.append(locationCode).append("-");

        String propertyTypeCode = "XXX";
        if (propertyDTO.getPropertyTypeId() != null && !propertyDTO.getPropertyTypeId().isEmpty()) {
            if (propertyDTO.getPropertyTypeId().length() >= 3) {
                propertyTypeCode = propertyDTO.getPropertyTypeId().substring(0, 3).toUpperCase();
            } else {
                propertyTypeCode = propertyDTO.getPropertyTypeId().toUpperCase();
            }
        }
        codeBuilder.append(propertyTypeCode).append("-");

        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        codeBuilder.append(timestamp);

        return codeBuilder.toString();
    }

    @Override
    public PropertyDTO create(PropertyDTO propertyDTO) throws Exception {
        if (propertyDTO.getCode() == null || propertyDTO.getCode().isEmpty()) {
            propertyDTO.setCode(generatePropertyCode(propertyDTO));
        }

        Property property = modelMapper.map(propertyDTO, Property.class);
        property.setDateCreated(new Date());
        property = propertyRepository.save(property);
        String propertyId = property.getPropertyId();

        if(propertyDTO.getCoordinatesDTO() != null){
            CoordinatesDTO coordinatesDTO = propertyDTO.getCoordinatesDTO();
            coordinatesDTO.setPropertyId(propertyId);

            Coordinates coordinates = modelMapper.map(coordinatesDTO, Coordinates.class);
            coodinatesRepository.save(coordinates);
        }

        if(propertyDTO.getDepositDTO() != null){
            DepositDTO depositDTO = propertyDTO.getDepositDTO();
            depositDTO.setPropertyId(propertyId);

            Deposit deposit = modelMapper.map(depositDTO, Deposit.class);
            depositsRepository.save(deposit);
        }

        if (propertyDTO.getPropertyPriceNewest() != null) {
            PropertyPriceHistoryDTO priceDTO = propertyDTO.getPropertyPriceNewest();
            priceDTO.setPropertyId(propertyId);

            PropertyPriceHistory priceHistory = modelMapper.map(priceDTO, PropertyPriceHistory.class);
            priceHistory.setDateCreated(new Date());
            priceHistory.setCreator(propertyDTO.getCreator());
            propertyPriceHistoryRepository.save(priceHistory);
        }

        if (propertyDTO.getAmenityDTOs() != null && !propertyDTO.getAmenityDTOs().isEmpty()) {
            List<PropertyAmenity> propertyAmenities = propertyDTO.getAmenityDTOs().stream()
                    .map(item -> {
                        PropertyAmenity amenity = modelMapper.map(item, PropertyAmenity.class);
                        amenity.setPropertyId(propertyId);
                        return amenity;
                    })
                    .collect(Collectors.toList());

            propertyAmenityRepository.saveAll(propertyAmenities);
        }

        if (propertyDTO.getPropertyFilesDTOs() != null && !propertyDTO.getPropertyFilesDTOs().isEmpty()) {
            List<PropertyFiles> propertyFiles = propertyDTO.getPropertyFilesDTOs().stream()
                    .map(fileDTO -> {
                        PropertyFiles files = new PropertyFiles();
                        files.setPropertyId(propertyId);
                        files.setCategory(fileDTO.getCategory());
                        files.setFileIds(fileDTO.getFileIds());
                        return files;
                    })
                    .collect(Collectors.toList());

            propertyFilesRepository.saveAll(propertyFiles);
        }

        PropertyDTO result = modelMapper.map(property, PropertyDTO.class);

        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setPropertyId(propertyId);
        result.setAmenityDTOs(amenityService.getAllPropertyAmenityBySearch(amenityDTO));

        result.setPropertyFilesDTOs(propertyFilesService.getAllByPropertyId(Collections.singletonList(propertyId)));

        result.setPropertyPriceNewest(propertyPriceHistoryService.findPriceNewestByPropertyIds(
                Collections.singletonList(propertyId)).stream().findFirst().orElse(null));

        result.setCoordinatesDTO(coordinatesService.findByPropertyId(propertyId));

        return result;
    }

    @Override
    @Transactional
    public PropertyDTO update(PropertyDTO propertyDTO) throws Exception {
        // Validate property exists
        Property existingProperty = propertyRepository.findById(propertyDTO.getPropertyId())
                .orElseThrow(() -> new Exception("Property not found with id: " + propertyDTO.getPropertyId()));

        String propertyId = existingProperty.getPropertyId();
        String existingCode = existingProperty.getCode();

        modelMapper.map(propertyDTO, existingProperty);

        existingProperty.setPropertyId(propertyId);

        if (propertyDTO.getCode() == null || propertyDTO.getCode().isEmpty()) {
            existingProperty.setCode(existingCode);
        }

        existingProperty.setDateModified(new Date());
        existingProperty = propertyRepository.save(existingProperty);

        // Cập nhật tọa độ
        if(propertyDTO.getCoordinatesDTO() != null){
            Coordinates existingCoordinates = coodinatesRepository.findByPropertyId(propertyId).orElse(new Coordinates());
            if (existingCoordinates.getPropertyId() == null) {
                existingCoordinates.setPropertyId(propertyId);
            }
            existingCoordinates.setLatitude(propertyDTO.getCoordinatesDTO().getLatitude());
            existingCoordinates.setLongitude(propertyDTO.getCoordinatesDTO().getLongitude());

            coodinatesRepository.save(existingCoordinates);
        }

        // Cập nhật deposit
        if(propertyDTO.getDepositDTO() != null){
            Deposit existingDeposit = depositsRepository.findByPropertyId(propertyId);
            if (existingDeposit == null) {
                existingDeposit = new Deposit();
                existingDeposit.setPropertyId(propertyId);
            }

            DepositDTO depositDTO = propertyDTO.getDepositDTO();

            existingDeposit.setPropertyId(propertyId);
            existingDeposit.setNote(depositDTO.getNote());
            existingDeposit.setValue(depositDTO.getValue());
            existingDeposit.setDueDate(depositDTO.getDueDate());
            existingDeposit.setScaleUnit(depositDTO.getScaleUnit());

            depositsRepository.save(existingDeposit);
        }

        // Update property price
        if (propertyDTO.getPropertyPriceNewest() != null) {
            PropertyPriceHistoryDTO priceDTO = propertyDTO.getPropertyPriceNewest();
            priceDTO.setPropertyId(propertyId);

            PropertyPriceHistory priceHistory = modelMapper.map(priceDTO, PropertyPriceHistory.class);

            priceHistory.setPropertyPriceHistoryId(null);
            priceHistory.setDateCreated(new Date());
            priceHistory.setCreator(propertyDTO.getCreator());
            propertyPriceHistoryRepository.save(priceHistory);
        }

        // Update amenities
        if (propertyDTO.getAmenityDTOs() != null) {
            try {
                List<PropertyAmenity> existingAmenities = propertyAmenityRepository.findByPropertyId(propertyId);

                if (existingAmenities != null && !existingAmenities.isEmpty()) {
                    propertyAmenityRepository.deleteAll(existingAmenities);
                    propertyAmenityRepository.flush();
                }

                // Thêm amenities mới
                if (!propertyDTO.getAmenityDTOs().isEmpty()) {
                    List<PropertyAmenity> propertyAmenities = propertyDTO.getAmenityDTOs().stream()
                            .map(item -> {
                                PropertyAmenity amenity = modelMapper.map(item, PropertyAmenity.class);
                                amenity.setPropertyAmenityId(null);
                                amenity.setPropertyId(propertyId);
                                return amenity;
                            })
                            .collect(Collectors.toList());

                    propertyAmenityRepository.saveAll(propertyAmenities);
                }
            } catch (Exception e) {
                throw new Exception("Error updating amenities: " + e.getMessage(), e);
            }
        }

        // Update property files
        if (propertyDTO.getPropertyFilesDTOs() != null) {
            try {
                List<PropertyFiles> existingFiles = propertyFilesRepository.findByPropertyId(propertyId);

                if (existingFiles != null && !existingFiles.isEmpty()) {
                    propertyFilesRepository.deleteAll(existingFiles);
                    propertyFilesRepository.flush();
                }

                // Thêm files mới
                if (!propertyDTO.getPropertyFilesDTOs().isEmpty()) {
                    List<PropertyFiles> propertyFiles = propertyDTO.getPropertyFilesDTOs().stream()
                            .map(fileDTO -> {
                                PropertyFiles files = new PropertyFiles();
                                files.setPropertyId(propertyId);
                                files.setCategory(fileDTO.getCategory());
                                files.setFileIds(fileDTO.getFileIds());
                                return files;
                            })
                            .collect(Collectors.toList());

                    propertyFilesRepository.saveAll(propertyFiles);
                }
            } catch (Exception e) {
                throw new Exception("Error updating property files: " + e.getMessage(), e);
            }
        }

        PropertyDTO result = modelMapper.map(existingProperty, PropertyDTO.class);

        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setPropertyId(propertyId);
        result.setAmenityDTOs(amenityService.getAllPropertyAmenityBySearch(amenityDTO));

        result.setPropertyFilesDTOs(propertyFilesService.getAllByPropertyId(Collections.singletonList(propertyId)));

        result.setPropertyPriceNewest(propertyPriceHistoryService.findPriceNewestByPropertyIds(
                Collections.singletonList(propertyId)).stream().findFirst().orElse(null));

        result.setCoordinatesDTO(coordinatesService.findByPropertyId(propertyId));

        result.setDepositDTO(depositService.getDepositsByPropertyId(propertyId));

        return result;
    }

    @Override
    @Transactional
    public boolean delete(String propertyId) throws Exception {
        try {
            // Check if property exists
            Property existingProperty = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new Exception("Property not found with id: " + propertyId));

            Coordinates existingCoordinates = coodinatesRepository.findByPropertyId(propertyId).orElse(new Coordinates());
            coodinatesRepository.delete(existingCoordinates);

            Deposit existingDeposit = depositsRepository.findByPropertyId(propertyId);
            depositsRepository.delete(existingDeposit);

            List<PropertyAmenity> existingAmenities = propertyAmenityRepository.findByPropertyId(propertyId);
            if (!existingAmenities.isEmpty()) {
                propertyAmenityRepository.deleteAll(existingAmenities);
                propertyAmenityRepository.flush();
            }

            List<PropertyFiles> existingFiles = propertyFilesRepository.findByPropertyId(propertyId);
            if (!existingFiles.isEmpty()) {
                propertyFilesRepository.deleteAll(existingFiles);
                propertyFilesRepository.flush();
            }

            List<PropertyPriceHistory> priceHistoryList = propertyPriceHistoryRepository.findByPropertyId(propertyId);
            if (!priceHistoryList.isEmpty()) {
                propertyPriceHistoryRepository.deleteAll(priceHistoryList);
                propertyPriceHistoryRepository.flush();
            }

            List<PropertyComment> propertyComments = propertyCommentRepository.findByPropertyId(propertyId);
            if (!propertyComments.isEmpty()) {
                propertyCommentRepository.deleteAll(propertyComments);
                propertyCommentRepository.flush();
            }

            propertyRepository.delete(existingProperty);

            return true;
        } catch (Exception e) {
            throw e;
        }
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
        Map<String, NativeSqlBuilder.ColumnInfo> columnInfoMap = NativeSqlBuilder.createColumnInfoMap();

        if(propertyDTO.getPriceFrom() == null) propertyDTO.setPriceFrom(Double.MIN_VALUE);
        if(propertyDTO.getPriceFrom() == null) propertyDTO.setPriceFrom(Double.MAX_VALUE);
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "priceFrom", "lp.Value", NativeSqlBuilder.ComparisonType.GREATER_THAN_OR_EQUAL);
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "priceTo", "lp.Value", NativeSqlBuilder.ComparisonType.LESS_THAN_OR_EQUAL);
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "priceNewestScale", "lp.ScaleUnit");
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "locationIds", "p.LocationId", NativeSqlBuilder.ComparisonType.IN);
        //NativeSqlBuilder.addColumnInfo(columnInfoMap, "statusIds", "p.StatusId", NativeSqlBuilder.ComparisonType.EQUAL);
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "creator", "p.Creator");
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "propertyTypeId", "pt.PropertyTypeId");
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "type", "p.Type");

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithColumnInfo(sqlSelect, propertyDTO, columnInfoMap);
        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithColumnInfo(sqlCount, propertyDTO, columnInfoMap);

        if(!Objects.equals(userService.getUserByUsername(SecurityUtils.getCurrentUserLogin().orElse("")).getRoleId(), "ADMIN")){
            nativeSqlAfterBuilded.params.put("isDeleted", 0);
            nativeSqlCount.params.put("isDeleted", 0);
        }
        else{
            nativeSqlAfterBuilded.params.put("isDeleted", null);
            nativeSqlCount.params.put("isDeleted", null);
        }

        if(!StringUtil.isNullOrEmpty(propertyDTO.getUsernameFav())){
            nativeSqlAfterBuilded.params.put("usernameFav", propertyDTO.getUsernameFav());
            nativeSqlCount.params.put("usernameFav", propertyDTO.getUsernameFav());
        }
        else{
            nativeSqlAfterBuilded.params.put("usernameFav", null);
            nativeSqlCount.params.put("usernameFav", null);
        }

        if(propertyDTO.getStatusIds() != null &&!propertyDTO.getStatusIds().isEmpty()){
//            nativeSqlAfterBuilded.params.put("statusIds", propertyDTO.getStatusIds());
//            nativeSqlCount.params.put("statusIds", propertyDTO.getStatusIds());
            ObjectMapper mapper = new ObjectMapper();
            String statusIdsJson = mapper.writeValueAsString(propertyDTO.getStatusIds());
            nativeSqlAfterBuilded.params.put("statusIds", statusIdsJson);
            nativeSqlCount.params.put("statusIds", statusIdsJson);
        }
        else{
            nativeSqlAfterBuilded.params.put("statusIds", null);
            nativeSqlCount.params.put("statusIds", null);
        }

        List<PropertyDTO> propertyDTOs = (List<PropertyDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, PropertyDTO.class);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        //Gán phần thông tin chung
        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setIsGeneralInfo(true);
        List<AmenityDTO> amenityDTOS = amenityService.getAllPropertyAmenityBySearch(amenityDTO);
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
    public Page<PropertyDTO> getPropertyFavourite(PropertyDTO propertyDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPropertyFavourite", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllPropertyFavourite", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        params.put("username", propertyDTO.getUsernameFav());
        params.put("username", propertyDTO.getUsernameFav());

        List<PropertyDTO> propertyDTOs = (List<PropertyDTO>) this.findAndAliasToBeanResultTransformer(sqlSelect, params, pageable, PropertyDTO.class);
        Long total = this.countByNativeQuery(sqlCount, params);

        return new PageImpl<>(propertyDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }

    @Override
    public List<PropertyDTO> getPropertyByLocation(String locationId) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllPropertyLocation", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        params.put("locationId", locationId);
        return (List<PropertyDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, PropertyDTO.class);
    }

    @Override
    public PropertyDTO getOneProperty(PropertyDTO propertyDTO) throws Exception {
        Optional<Property> entity = propertyRepository.findById(propertyDTO.getPropertyId());
        if(entity.isPresent()){
            Property property = entity.get();
            PropertyDTO result = modelMapper.map(property, PropertyDTO.class);

            AmenityDTO amenityDTO = new AmenityDTO();
            amenityDTO.setPropertyId(result.getPropertyId());
            result.setAmenityDTOs(amenityService.getAllPropertyAmenityBySearch(amenityDTO));

            result.setPropertyFilesDTOs(propertyFilesService.getAllByPropertyId(Collections.singletonList(property.getPropertyId())));
            result.setPropertyPriceNewest(propertyPriceHistoryService.findPriceNewestByPropertyIds(Collections.singletonList(property.getPropertyId())).getFirst());
            result.setPropertyPriceHistoryDTOs(propertyPriceHistoryService.findByPropertyIds(Collections.singletonList(property.getPropertyId())));
            result.setDepositDTO(depositService.getDepositsByPropertyId(property.getPropertyId()));
            result.setCoordinatesDTO(coordinatesService.findByPropertyId(property.getPropertyId()));
            result.setPublicFacilityDTOs(publicFacilityService.findByPropertyId(property.getPropertyId()));
            result.setPropertyCommentDTOs(propertyCommentService.getPropertyCommentByPropertyId(property.getPropertyId()));
            result.setApprovalHistoryDTOs(approvalHistoryService.findByPropertyId(property.getPropertyId()));

            return result;
        }
        throw new Exception("Property not found with ID: " + propertyDTO.getPropertyId());
    }

    @Override
    @Transactional
    public boolean approve(PropertyDTO propertyDTO) throws Exception {
        if (propertyDTO.getPropertyId() == null) {
            throw new Exception("Property ID cannot be null");
        }

        Property existingProperty = propertyRepository.findById(propertyDTO.getPropertyId())
                .orElseThrow(() -> new Exception("Property not found with id: " + propertyDTO.getPropertyId()));

        if (propertyDTO.getApprovalHistoryDTO() != null) {
            //update property status
            List<String> propertyStatus = new ArrayList<>();
            propertyStatus.add(propertyDTO.getApprovalHistoryDTO().getStatusId());
            existingProperty.setStatusIds(propertyStatus);
            propertyRepository.save(existingProperty);

            //update ApprovalHistory
            ApprovalHistory approvalHistory =  modelMapper.map(propertyDTO.getApprovalHistoryDTO(), ApprovalHistory.class);
            approvalHistory.setPropertyId(existingProperty.getPropertyId());
            approvalHistory.setApprovalDate(new Date());
            approvalHistoryRepository.save(approvalHistory);

            return true;
        }

        return false;
    }

    @Override
    public boolean addToFavourite(PropertyDTO propertyDTO) throws Exception {
        if(!StringUtil.isNullOrEmpty(propertyDTO.getPropertyId())){
            UserFavourite userFavourite = new UserFavourite();
            userFavourite.setPropertyId(propertyDTO.getPropertyId());
            userFavourite.setUsername(propertyDTO.getUsernameFav());
            userFavouriteRepository.save(userFavourite);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeToFavourite(PropertyDTO propertyDTO) throws Exception {
        Optional<UserFavourite> userFavouriteExisting = userFavouriteRepository.findByPropertyIdAndUsername(propertyDTO.getPropertyId(), propertyDTO.getUsernameFav());
        if(userFavouriteExisting.isPresent()){
            UserFavourite userFavourite = userFavouriteExisting.get();
            userFavouriteRepository.delete(userFavourite);
            return true;
        }
        return false;
    }

    @Override
    public boolean lock(PropertyDTO propertyDTO) throws Exception {
        Optional<Property> existingProperty = propertyRepository.findById(propertyDTO.getPropertyId());
        if(existingProperty.isPresent()){
            Property property = existingProperty.get();
            property.setIsDeleted(1);
            propertyRepository.save(property);
            return true;
        }
        return existingProperty.isPresent();
    }

    @Override
    public boolean unLock(PropertyDTO propertyDTO) throws Exception {
        Optional<Property> existingProperty = propertyRepository.findById(propertyDTO.getPropertyId());
        if(existingProperty.isPresent()){
            Property property = existingProperty.get();
            property.setIsDeleted(0);
            propertyRepository.save(property);
            return true;
        }
        return existingProperty.isPresent();
    }

}
