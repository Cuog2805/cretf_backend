package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> {
}
