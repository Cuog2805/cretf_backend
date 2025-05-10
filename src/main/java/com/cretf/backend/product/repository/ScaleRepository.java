package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScaleRepository extends JpaRepository<Scale, String> {
}
