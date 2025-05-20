package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.PropertyFiles;
import com.cretf.backend.product.entity.PropertyPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPriceHistoryRepository extends JpaRepository<PropertyPriceHistory, String> {
    @Query("SELECT p FROM PropertyPriceHistory p WHERE p.propertyId IN :propertyIds ORDER BY p.dateCreated DESC")
    List<PropertyPriceHistory> findByPropertyIds(@Param("propertyIds") List<String> propertyIds);

    @Query("SELECT p FROM PropertyPriceHistory p WHERE p.propertyId = :propertyId")
    List<PropertyPriceHistory> findByPropertyId(@Param("propertyId") String propertyId);
}
