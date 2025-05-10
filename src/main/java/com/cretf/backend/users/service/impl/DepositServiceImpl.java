package com.cretf.backend.users.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.users.dto.DepositDTO;
import com.cretf.backend.users.entity.Deposit;
import com.cretf.backend.users.repository.DepositsRepository;
import com.cretf.backend.users.service.DepositService;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DepositServiceImpl extends BaseJdbcServiceImpl<DepositDTO, String> implements DepositService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Deposit";

    private final DepositsRepository depositsRepository;
    private final ModelMapper modelMapper;

    public DepositServiceImpl(
            EntityManager entityManager,
            DepositsRepository depositsRepository,
            ModelMapper modelMapper
    ) {
        super(entityManager, DepositDTO.class);
        this.depositsRepository = depositsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<DepositDTO> getDepositsBySearch(DepositDTO depositsDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllDeposit", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllDeposit", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, NativeSqlBuilder.ColumnInfo> columnInfoMap = NativeSqlBuilder.createColumnInfoMap();

        NativeSqlBuilder.addColumnInfo(columnInfoMap, "statusId", "d.StatusId", NativeSqlBuilder.ComparisonType.EQUAL);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithColumnInfo(sqlSelect, depositsDTO, columnInfoMap);
        List<DepositDTO> result = (List<DepositDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, DepositDTO.class);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithColumnInfo(sqlCount, depositsDTO, columnInfoMap);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }


    @Override
    public DepositDTO getDepositsById(String id) throws Exception {
        Deposit deposit = depositsRepository.findById(id).orElseThrow(() -> new Exception("Deposit not found"));
        return modelMapper.map(deposit, DepositDTO.class);
    }

    @Override
    public DepositDTO getDepositsByPropertyId(String propertyTd) throws Exception {
        Deposit deposit = depositsRepository.findByPropertyId(propertyTd);
        return modelMapper.map(deposit, DepositDTO.class);
    }

}
