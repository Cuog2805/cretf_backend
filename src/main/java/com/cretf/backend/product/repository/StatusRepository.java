package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
    @Query("SELECT s FROM Status s WHERE s.code = :code and s.type = :type and s.isDeleted = 0")
    Optional<Status> findByCodeAndType(@Param("code") String code, @Param("type") String type);
}
