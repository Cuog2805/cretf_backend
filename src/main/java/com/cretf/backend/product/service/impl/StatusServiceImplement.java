package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.entity.Status;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.product.service.StatusService;
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

@Service
public class StatusServiceImplement extends BaseJdbcServiceImpl<StatusDTO, String> implements StatusService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Status";

    private final ModelMapper modelMapper;
    private final StatusRepository statusRepository;

    public StatusServiceImplement(
            EntityManager entityManager,
            ModelMapper modelMapper,
            StatusRepository statusRepository) {
        super(entityManager, StatusDTO.class);
        this.modelMapper = modelMapper;
        this.statusRepository = statusRepository;
    }


    @Override
    public Page<StatusDTO> getStatusBySearch(StatusDTO statusDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllStatus", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllStatus", FILE_EXTENSION, FILE_PATH_NAME);
        //param
        Map<String, String> aliasMap = new HashMap<>();
        //aliasMap.put("propertyId", "pa");
        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithParams(sqlSelect, statusDTO, aliasMap);
        List<StatusDTO> statusDTOs = (List<StatusDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, StatusDTO.class);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithParams(sqlCount, statusDTO, aliasMap);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        return new PageImpl<>(statusDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }
}
