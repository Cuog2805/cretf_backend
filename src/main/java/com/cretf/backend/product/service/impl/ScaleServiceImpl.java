package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.ScaleDTO;
import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.repository.ScaleRepository;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.product.service.ScaleService;
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
public class ScaleServiceImpl extends BaseJdbcServiceImpl<ScaleDTO, String> implements ScaleService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Scale";

    private final ModelMapper modelMapper;
    private final ScaleRepository scaleRepository;

    public ScaleServiceImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            ScaleRepository scaleRepository
    ) {
        super(entityManager, ScaleDTO.class);
        this.modelMapper = modelMapper;
        this.scaleRepository = scaleRepository;
    }

    @Override
    public Page<ScaleDTO> getScaleBySearch(ScaleDTO scaleDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllScale", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllScale", FILE_EXTENSION, FILE_PATH_NAME);
        //param
        Map<String, NativeSqlBuilder.ColumnInfo> columnInfoMap = NativeSqlBuilder.createColumnInfoMap();
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "Type", "s.type");

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithColumnInfo(sqlSelect, scaleDTO, columnInfoMap);
        List<ScaleDTO> scaleDTOS = (List<ScaleDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, ScaleDTO.class);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithColumnInfo(sqlCount, scaleDTO, columnInfoMap);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        return new PageImpl<>(scaleDTOS, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }
}
