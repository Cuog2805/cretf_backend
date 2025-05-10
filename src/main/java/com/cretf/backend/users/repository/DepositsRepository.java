package com.cretf.backend.users.repository;

import com.cretf.backend.product.entity.PropertyPriceHistory;
import com.cretf.backend.users.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositsRepository extends JpaRepository<Deposit, String> {
    @Query("SELECT d FROM Deposit d WHERE d.propertyId = :propertyId")
    Deposit findByPropertyId(@Param("propertyId") String propertyId);
}
