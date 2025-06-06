package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.PropertyAmenity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyAmenityRepository extends JpaRepository<PropertyAmenity, String> {
    @Query("SELECT p FROM PropertyAmenity p WHERE p.propertyId = :propertyId")
    List<PropertyAmenity> findByPropertyId(@Param("propertyId") String propertyId);
}
