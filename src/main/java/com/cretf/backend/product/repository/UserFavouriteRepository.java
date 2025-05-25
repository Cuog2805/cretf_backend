package com.cretf.backend.product.repository;

import com.cretf.backend.product.entity.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserFavouriteRepository extends JpaRepository<UserFavourite, String> {
    @Query("SELECT uf FROM UserFavourite uf WHERE uf.propertyId = :propertyId AND uf.username = :username")
    Optional<UserFavourite> findByPropertyIdAndUsername(@Param("propertyId") String propertyId, @Param("username") String username);
}
