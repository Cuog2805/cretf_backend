package com.cretf.backend.users.repository;

import com.cretf.backend.users.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, String> {
    @Query("SELECT ud FROM UserDetail ud WHERE ud.userId = :userId")
    Optional<UserDetail> findByUserId(@Param("userId") String userId);
}
