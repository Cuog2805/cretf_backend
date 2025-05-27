package com.cretf.backend.users.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.security.SecurityUtils;
import com.cretf.backend.users.dto.RoleDTO;
import com.cretf.backend.users.entity.Role;
import com.cretf.backend.users.entity.Users;
import com.cretf.backend.users.repository.RoleRepository;
import com.cretf.backend.users.repository.UsersRepository;
import com.cretf.backend.users.service.RoleService;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleSericeImpl extends BaseJdbcServiceImpl<RoleDTO, String> implements RoleService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Role";

    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;

    public RoleSericeImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            RoleRepository roleRepository,
            UsersRepository usersRepository
    ) {
        super(entityManager, RoleDTO.class);
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public Page<RoleDTO> getRoleBySearch(RoleDTO roleDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllRole", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllRole", FILE_EXTENSION, FILE_PATH_NAME);
        //param
        Map<String, NativeSqlBuilder.ColumnInfo> columnInfoMap = NativeSqlBuilder.createColumnInfoMap();
        NativeSqlBuilder.addColumnInfo(columnInfoMap, "name", "r.Name", NativeSqlBuilder.ComparisonType.LIKE);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlAfterBuilded = NativeSqlBuilder.buildSqlWithColumnInfo(sqlSelect, roleDTO, columnInfoMap);
        List<RoleDTO> statusDTOs = (List<RoleDTO>) this.findAndAliasToBeanResultTransformer(nativeSqlAfterBuilded.sql, nativeSqlAfterBuilded.params, pageable, RoleDTO.class);

        NativeSqlBuilder.NativeSqlAfterBuilded nativeSqlCount = NativeSqlBuilder.buildSqlWithColumnInfo(sqlCount, roleDTO, columnInfoMap);
        Long total = this.countByNativeQuery(nativeSqlCount.sql, nativeSqlCount.params);

        return new PageImpl<>(statusDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }

    @Override
    public boolean delete(String id) throws Exception {
        List<Users> usersList = usersRepository.findByRoleId(id);
        if(!usersList.isEmpty()){
            throw new Exception("Role is in used");
        }
        Optional<Role> existing = roleRepository.findById(id);
        if(existing.isPresent()){
            Role role = existing.get();
            role.setIsDeleted(1);
            roleRepository.save(role);
            return true;
        }
        return existing.isPresent();
    }

    @Override
    public boolean restore(String id) throws Exception {
        Optional<Role> existing = roleRepository.findById(id);
        if(existing.isPresent()){
            Role role = existing.get();
            role.setIsDeleted(0);
            roleRepository.save(role);
            return true;
        }
        return existing.isPresent();
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) throws Exception {
        Role role = modelMapper.map(roleDTO, Role.class);
        role.setRoleId(roleDTO.getName().toUpperCase());
        role.setDateCreated(new Date());
        role.setCreator(SecurityUtils.getCurrentUserLogin().orElse(null));
        role = roleRepository.save(role);
        return modelMapper.map(role, RoleDTO.class);
    }


    @Override
    public RoleDTO update(RoleDTO roleDTO) throws Exception {
        Role existing = roleRepository.findById(roleDTO.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found with " + roleDTO.getRoleId()));
        Role role = modelMapper.map(roleDTO, Role.class);
        role.setCreator(existing.getCreator());
        role.setDateCreated(existing.getDateCreated());
        role.setDateModified(new Date());
        role.setModifier(SecurityUtils.getCurrentUserLogin().orElse(null));
        role = roleRepository.save(role);
        return modelMapper.map(role, RoleDTO.class);
    }

}
