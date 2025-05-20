package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    public Page<RoleDTO> getRoleBySearch(RoleDTO roleDTO, Pageable pageable) throws Exception;
    public boolean lock(String id) throws Exception;
    public boolean unlock(String id) throws Exception;
    public RoleDTO create(RoleDTO roleDTO) throws Exception;
    public RoleDTO update(RoleDTO roleDTO) throws Exception;
}
