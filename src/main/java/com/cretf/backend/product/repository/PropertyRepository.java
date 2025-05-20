package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, String> {
//    @Query("SELECT p FROM Property p WHERE p.locationId = :locationId")
//    List<Property> findByLocationId(@Param("locationId") String locationId);
}
