package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.ApprovalHistory;
import com.cretf.backend.product.entity.PropertyPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, String> {
    @Query("SELECT ah FROM ApprovalHistory ah WHERE ah.propertyId = :propertyId ORDER BY ah.approvalDate DESC")
    List<ApprovalHistory> findByPropertyId(@Param("propertyId") String propertyId);
}
