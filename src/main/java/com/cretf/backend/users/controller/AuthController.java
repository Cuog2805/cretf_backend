package com.cretf.backend.users.controller;

import com.cretf.backend.security.JwtUtil;
import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.repository.UsersRepository;
import com.cretf.backend.users.service.impl.AuthService;
import com.cretf.backend.utils.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Response<UsersDTO> login(@RequestBody UsersDTO request) throws Exception {
        UsersDTO result = authService.login(request);
        return Response.ok(result);
    }

    @PostMapping("/admin/login")
    public Response<UsersDTO> adminLogin(@RequestBody UsersDTO request) throws Exception {
        UsersDTO result = authService.loginAdmin(request);
        return Response.ok(result);
    }

    @PostMapping("/register")
    public Response<UsersDTO> register(@RequestBody UsersDTO request) throws Exception {
        UsersDTO result = authService.register(request);
        return Response.ok(result);
    }

    @PostMapping("/logout")
    public Response<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return Response.ok("Logout succeed");
    }

    @PostMapping("/change-password")
    public Response<String> changePassword(@RequestBody UsersDTO request) throws Exception {
        authService.changePassword(request);
        return Response.ok("Change password succeed");
    }
}

