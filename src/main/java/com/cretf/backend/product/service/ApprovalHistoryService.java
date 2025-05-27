package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.ApprovalHistoryDTO;

import java.util.List;

public interface ApprovalHistoryService {
    List<ApprovalHistoryDTO> findByEntityTableId(String entityTableId) throws Exception;
    List<ApprovalHistoryDTO> findByEntityTableIds(List<String> entityTableIds) throws Exception;
    public List<ApprovalHistoryDTO> findNewestApprovalByEntityTableIds(List<String> entityTableIds) throws Exception;
}
