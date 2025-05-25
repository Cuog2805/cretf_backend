package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.ApprovalHistoryDTO;
import com.cretf.backend.product.entity.ApprovalHistory;
import com.cretf.backend.product.repository.ApprovalHistoryRepository;
import com.cretf.backend.product.repository.PropertyPriceHistoryRepository;
import com.cretf.backend.product.service.ApprovalHistoryService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApprovalHistoryServiceImpl extends BaseJdbcServiceImpl<ApprovalHistoryDTO, String> implements ApprovalHistoryService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/ApprovalHistory";

    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final ModelMapper modelMapper;

    public ApprovalHistoryServiceImpl(EntityManager entityManager, ApprovalHistoryRepository approvalHistoryRepository, ModelMapper modelMapper) {
        super(entityManager, ApprovalHistoryDTO.class);
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<ApprovalHistoryDTO> findByPropertyId(String propertyId) throws Exception {
        List<ApprovalHistory> approvalHistories = approvalHistoryRepository.findByPropertyId(propertyId);

        List<ApprovalHistoryDTO> approvalHistoryDTOs = approvalHistories.stream().map(item -> {
            return modelMapper.map(item, ApprovalHistoryDTO.class);
        }).collect(Collectors.toList());

        return approvalHistoryDTOs;
    }

    @Override
    public List<ApprovalHistoryDTO> findNewestApprovalByPropertyIds(List<String> propertyIds) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllApprovalHistoryNewest", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, Object> params = new HashMap<>();
        params.put("propertyIds", propertyIds);

        List<ApprovalHistoryDTO> approvalHistoryDTOs = (List<ApprovalHistoryDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, ApprovalHistoryDTO.class);

        return approvalHistoryDTOs;
    }
}
