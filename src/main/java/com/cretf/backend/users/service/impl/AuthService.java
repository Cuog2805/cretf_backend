package com.cretf.backend.users.service.impl;

import com.cretf.backend.product.entity.Status;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.security.JwtUtil;
import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.entity.Role;
import com.cretf.backend.users.entity.UserDetail;
import com.cretf.backend.users.entity.Users;
import com.cretf.backend.users.repository.RoleRepository;
import com.cretf.backend.users.repository.UserDetailRepository;
import com.cretf.backend.users.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    //private final UserService userService;
    private final UsersRepository usersRepository;
    private final UserDetailRepository userDetailRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public UsersDTO login(UsersDTO dto) throws Exception {
        if (dto.getIsDeleted() == 1) {
            throw new Exception("You account is locked!!");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        Users user = (Users) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);

        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
        usersDTO.setToken(token);

        return usersDTO;
    }

    public UsersDTO loginAdmin(UsersDTO request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Users user = (Users) authentication.getPrincipal();

        // Kiểm tra quyền ADMIN
        if (!"ADMIN".equals(user.getRoleId())) {
            throw new Exception("You don't have role to access");
        }

        String token = jwtUtil.generateToken(user);

        UsersDTO response = modelMapper.map(user, UsersDTO.class);
        response.setToken(token);
        return response;
    }


    public UsersDTO register(UsersDTO dto) throws Exception {
        // Kiểm tra username và email đã tồn tại chưa
        if (usersRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        Users user = modelMapper.map(dto, Users.class);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);


        // Lấy role va status mặc định cho user mới
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new Exception("Default role not found"));
        Status userStatus = statusRepository.findByCodeAndType("ACTIVE", "USER_STATUS")
                .orElseThrow(() -> new Exception("Default status not found"));
        user.setRoleId(userRole.getRoleId());
        user.setStatusId(userStatus.getStatusId());

        user.setDateCreated(new Date());
        user.setCreator(dto.getUsername());
        Status statusUserActive = statusRepository.findByCodeAndType("ACTIVE", "USER_STATUS").orElse(new Status());
        user.setStatusId(statusUserActive.getStatusId());
        // save user
        Users savedUser = usersRepository.save(user);

        UserDetail userDetailSaved = modelMapper.map(dto.getUserDetailDTO(), UserDetail.class);
        userDetailSaved.setUserId(user.getUserId());
        userDetailRepository.save(userDetailSaved);

        // Tạo token
        String token = jwtUtil.generateToken(savedUser);

        dto = modelMapper.map(user, UsersDTO.class);
        dto.setToken(token);

        return dto;
    }

    public void logout(HttpServletRequest request) {
        // Xóa thông tin xác thực khỏi SecurityContext
        SecurityContextHolder.clearContext();
    }

    public void changePassword(UsersDTO dto) throws Exception {
        // Authenticate user
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Invalid old password");
        }

        Users user = usersRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(hashedPassword);

        user.setDateModified(new Date());
        user.setModifier(dto.getUsername());

        usersRepository.save(user);
    }
}
