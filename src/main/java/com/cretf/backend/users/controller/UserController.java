package com.cretf.backend.users.controller;

import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.entity.Users;
import com.cretf.backend.users.service.impl.UserService;
import com.cretf.backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<List<UsersDTO>> getAllUsers(@RequestBody UsersDTO usersDTO, @ParameterObject Pageable pageable) {
        return Response.ok(userService.getAllUsers(usersDTO, pageable));
    }

    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public Response<UsersDTO> getUserById(@PathVariable String userId) throws Exception {
        return Response.ok(userService.getUserById(userId));
    }

    @PostMapping("/updateUser")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public Response<UsersDTO> updateUser(
            @Valid @RequestBody UsersDTO request) throws Exception {
        return Response.ok(userService.updateUser(request));
    }

    @PostMapping("/lockUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> lockUser(@PathVariable String userId) throws Exception {
        boolean result = userService.lockUser(userId);
        if (result) {
            return Response.ok("Lock succeed!");
        }
        throw new Exception("Lock fail!");
    }

    @PostMapping("/unlockUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> unlockUser(@PathVariable String userId) throws Exception {
        boolean result = userService.unlockUser(userId);
        if (result) {
            return Response.ok("Unlock succeed!");
        }
        throw new Exception("Unlock fail!");
    }

    @DeleteMapping("/deleteUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> deleteUser(@PathVariable String userId) throws Exception {
        boolean result = userService.deleteUser(userId);
        if (result) {
            return Response.ok("Delete succeed!");
        }
        throw new Exception("Delete fail!");
    }

    @DeleteMapping("/restoreUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> restoreUser(@PathVariable String userId) throws Exception {
        boolean result = userService.restoreUser(userId);
        if (result) {
            return Response.ok("Restore succeed!");
        }
        throw new Exception("Restore fail!");
    }

    @GetMapping("/me")
    public Response<UsersDTO> getCurrentUser(Authentication authentication) throws Exception {
        Users user = (Users) authentication.getPrincipal();
        return Response.ok(userService.getUserById(user.getUserId()));
    }
}