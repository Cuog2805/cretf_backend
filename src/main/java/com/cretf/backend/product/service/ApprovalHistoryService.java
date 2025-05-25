package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.ApprovalHistoryDTO;

import java.util.List;

public interface ApprovalHistoryService {
    List<ApprovalHistoryDTO> findByPropertyId(String propertyId) throws Exception;
    public List<ApprovalHistoryDTO> findNewestApprovalByPropertyIds(List<String> propertyIds) throws Exception;
}
