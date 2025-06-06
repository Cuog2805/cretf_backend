package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.PropertyNearbyPlace;
import com.cretf.backend.product.entity.PublicFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicFacilityRepository extends JpaRepository<PublicFacility, String> {
    @Query("SELECT pnb FROM PublicFacility pnb WHERE pnb.locationId = :locationId")
    List<PublicFacility> findByLocationId(@Param("locationId") String locationId);
}
