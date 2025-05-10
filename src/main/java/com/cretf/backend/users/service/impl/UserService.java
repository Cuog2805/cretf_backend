package com.cretf.backend.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.users.dto.UserDetailDTO;
import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.entity.UserDetail;
import com.cretf.backend.users.entity.Users;
import com.cretf.backend.users.repository.UserDetailRepository;
import com.cretf.backend.users.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final UserDetailRepository userDetailRepository;
    private final ModelMapper modelMapper;

    public UserService(
            UsersRepository usersRepository,
            UserDetailRepository userDetailRepository,
            ModelMapper modelMapper
    ) {
        this.usersRepository = usersRepository;
        this.userDetailRepository = userDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll().stream().toList();
        List<UsersDTO> usersDTOs = (List<UsersDTO>) users.stream().map(item -> {
            return modelMapper.map(item, UsersDTO.class);
        });
        return usersDTOs;
    }

    public UsersDTO getUserById(String userId) throws Exception {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id: " + userId));
        UserDetail userDetail = userDetailRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("UserDetail not found with id: " + userId));

        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
        UserDetailDTO userDetailDTO = modelMapper.map(userDetail, UserDetailDTO.class);
        usersDTO.setUserDetailDTO(userDetailDTO);

        return usersDTO;
    }

    public UsersDTO getUserByUsername(String username) throws Exception {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found with username: " + username));
        UserDetail userDetail = userDetailRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new Exception("UserDetail not found with username: " + username));

        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
        UserDetailDTO userDetailDTO = modelMapper.map(userDetail, UserDetailDTO.class);
        usersDTO.setUserDetailDTO(userDetailDTO);

        return usersDTO;
    }

    public UsersDTO updateUser(UsersDTO usersDTO) throws Exception {
        // Validate input
        if (usersDTO == null || StringUtil.isNullOrEmpty(usersDTO.getUserId())) {
            throw new Exception("Invalid user information");
        }

        // Find existing entities
        Users existingUser = usersRepository.findById(usersDTO.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        UserDetail existingUserDetail = userDetailRepository.findByUserId(usersDTO.getUserId())
                .orElseThrow(() -> new Exception("UserDetail not found"));

        String existingPassword = existingUser.getPassword();
        String existingUsername = existingUser.getUsername();
        Date existingDateCreated = existingUser.getDateCreated();
        String existingCreator = existingUser.getCreator();
        String existingRoleId = existingUser.getRoleId();

        BeanUtils.copyProperties(usersDTO, existingUser);

        existingUser.setPassword(existingPassword);
        existingUser.setDateCreated(existingDateCreated);
        existingUser.setCreator(existingCreator);

        if (StringUtil.isNullOrEmpty(usersDTO.getUsername())) {
            existingUser.setUsername(existingUsername);
        }

        if (StringUtil.isNullOrEmpty(usersDTO.getRoleId())) {
            existingUser.setRoleId(existingRoleId);
        }

        existingUser.setDateModified(new Date());
        if (StringUtil.isNullOrEmpty(usersDTO.getModifier())) {
            existingUser.setModifier(existingUsername);
        }

        Users savedUser = usersRepository.save(existingUser);

        // Update user detail entity
        if (usersDTO.getUserDetailDTO() != null) {
            UserDetailDTO detailDTO = usersDTO.getUserDetailDTO();

            // Store the existing userDetailId
            String existingUserDetailId = existingUserDetail.getUserDetailId();

            BeanUtils.copyProperties(detailDTO, existingUserDetail);

            existingUserDetail.setUserDetailId(existingUserDetailId);
            existingUserDetail.setUserId(usersDTO.getUserId());

            UserDetail savedUserDetail = userDetailRepository.save(existingUserDetail);

            // Map to DTO for response
            UsersDTO result = modelMapper.map(savedUser, UsersDTO.class);
            UserDetailDTO userDetailDTO = modelMapper.map(savedUserDetail, UserDetailDTO.class);
            result.setUserDetailDTO(userDetailDTO);

            return result;
        } else {
            return modelMapper.map(savedUser, UsersDTO.class);
        }
    }

    public void deleteUser(String userId) throws Exception {
        if (!usersRepository.existsById(userId)) {
            throw new Exception("User not found with id: " + userId);
        }
        usersRepository.deleteById(userId);
    }
}
