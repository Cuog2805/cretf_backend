package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, String> {
}
