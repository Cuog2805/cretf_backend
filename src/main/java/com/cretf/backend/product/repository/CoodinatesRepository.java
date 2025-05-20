package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface CoodinatesRepository extends JpaRepository<Coordinates, String> {
    @Query("SELECT c FROM Coordinates c WHERE c.propertyId = :propertyId")
    Optional<Coordinates> findByPropertyId(@Param("propertyId") String propertyId);
}
