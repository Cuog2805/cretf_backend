package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.CategoryShared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySharedRepository extends JpaRepository<CategoryShared, String> {
}
