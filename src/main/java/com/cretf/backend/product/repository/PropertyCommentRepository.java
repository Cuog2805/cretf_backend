package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.PropertyComment;
import com.cretf.backend.product.entity.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyCommentRepository extends JpaRepository<PropertyComment, String> {
    @Query("SELECT pc FROM PropertyComment pc WHERE pc.propertyId = :propertyId")
    List<PropertyComment> findByPropertyId(@Param("propertyId") String propertyId);

    @Query("SELECT pc FROM PropertyComment pc WHERE pc.code = :code")
    Optional<PropertyComment> findByCode(@Param("code") String code);
}
