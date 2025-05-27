package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.ApprovalHistory;
import com.cretf.backend.product.entity.PropertyPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, String> {
    @Query("SELECT ah FROM ApprovalHistory ah WHERE ah.entityTableId = :entityTableId ORDER BY ah.approvalDate DESC")
    List<ApprovalHistory> findByEntityTableId(@Param("entityTableId") String entityTableId);

    @Query("SELECT ah FROM ApprovalHistory ah WHERE ah.entityTableId IN :entityTableIds ORDER BY ah.approvalDate DESC")
    List<ApprovalHistory> findByEntityTableIds(@Param("entityTableIds") List<String> entityTableIds);
}
