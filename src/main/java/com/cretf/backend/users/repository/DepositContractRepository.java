package com.cretf.backend.users.repository;

import com.cretf.backend.users.entity.DepositContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositContractRepository extends JpaRepository<DepositContract, String> {
    @Query("SELECT d FROM DepositContract d WHERE d.seller LIKE %:keyword% OR d.buyer LIKE %:keyword%")
    List<DepositContract> findBySellerNameContainingOrBuyerNameContaining(@Param("keyword") String keyword);
}
