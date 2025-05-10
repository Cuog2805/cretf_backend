package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.PropertyFiles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyFilesRepository extends JpaRepository<PropertyFiles, String> {
    @Query("SELECT p FROM PropertyFiles p WHERE p.propertyId IN :propertyIds")
    List<PropertyFiles> findByPropertyIds(@Param("propertyIds") List<String> propertyIds);

    @Query("SELECT p FROM PropertyFiles p WHERE p.propertyId = :propertyId")
    List<PropertyFiles> findByPropertyId(@Param("propertyId") String propertyId);
}
