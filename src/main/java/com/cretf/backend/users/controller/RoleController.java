package com.cretf.backend.users.controller;

import com.cretf.backend.product.dto.StatusDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.StatusService;
import com.cretf.backend.users.dto.RoleDTO;
import com.cretf.backend.users.repository.UsersRepository;
import com.cretf.backend.users.service.RoleService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final RoleService roleService;
    private final UsersRepository usersRepository;

    public RoleController(RoleService roleService, UsersRepository usersRepository) {
        this.roleService = roleService;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/getAllRoles")
    public Response<List<RoleDTO>> getAllRoles(@RequestBody RoleDTO roleDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllRoles");
        Page<RoleDTO> result = roleService.getRoleBySearch(roleDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("/deleteRole/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> deleteRole(@PathVariable String id) throws Exception {

        boolean result = roleService.delete(id);
        if (result) {
            return Response.ok("delete succeed!");
        }
        throw new Exception("delete fail!");
    }

    @PostMapping("/restoreRole/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> restoreRole(@PathVariable String id) throws Exception {
        boolean result = roleService.restore(id);
        if (result) {
            return Response.ok("restore succeed!");
        }
        throw new Exception("restore fail!");
    }

    @PostMapping("/createRole")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) throws Exception {
        RoleDTO result = roleService.create(roleDTO);
        return Response.ok(result);
    }

    @PostMapping("/updateRole")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<RoleDTO> updateRole(@RequestBody RoleDTO roleDTO) throws Exception {
        RoleDTO result = roleService.update(roleDTO);
        return Response.ok(result);
    }
}
