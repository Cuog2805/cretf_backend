package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Property, String> {
}
