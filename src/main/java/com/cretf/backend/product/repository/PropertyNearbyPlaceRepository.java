package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.ApprovalHistory;
import com.cretf.backend.product.entity.PropertyNearbyPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyNearbyPlaceRepository extends JpaRepository<PropertyNearbyPlace, String> {
    @Query("SELECT pnb FROM PropertyNearbyPlace pnb WHERE pnb.publicFacilityId = :publicFacilityId")
    List<PropertyNearbyPlace> findByPublicFacilityId(@Param("publicFacilityId") String publicFacilityId);
}
