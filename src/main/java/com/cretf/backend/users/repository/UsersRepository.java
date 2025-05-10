package com.cretf.backend.users.repository;

import com.cretf.backend.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
     @Query("SELECT u FROM Users u WHERE u.username = :username")
     Optional<Users> findByUsername(@Param("username") String username);

     @Query("SELECT u FROM Users u WHERE u.email = :email")
     Optional<Users> findByEmail(@Param("email") String email);
}
