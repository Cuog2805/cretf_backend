package com.cretf.backend.users.repository;

import com.cretf.backend.users.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
