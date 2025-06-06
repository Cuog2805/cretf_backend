package com.cretf.backend.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.product.entity.Status;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.users.dto.UserDetailDTO;
import com.cretf.backend.users.dto.UsersDTO;
import com.cretf.backend.users.entity.UserDetail;
import com.cretf.backend.users.entity.Users;
import com.cretf.backend.users.repository.UserDetailRepository;
import com.cretf.backend.users.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final UserDetailRepository userDetailRepository;
    private final ModelMapper modelMapper;
    private final StatusRepository statusRepository;

    public UserService(
            UsersRepository usersRepository,
            UserDetailRepository userDetailRepository,
            ModelMapper modelMapper,
            StatusRepository statusRepository
    ) {
        this.usersRepository = usersRepository;
        this.userDetailRepository = userDetailRepository;
        this.modelMapper = modelMapper;
        this.statusRepository = statusRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Page<UsersDTO> getAllUsers(UsersDTO usersDTO, Pageable pageable) {
        String username = usersDTO.getUsername() != null ? usersDTO.getUsername().toLowerCase() : "";

        List<Users> users = usersRepository.findAll().stream()
                .filter(item -> {
                    return item.getUsername().toLowerCase().contains(username);
                })
                .toList();

        List<UsersDTO> usersDTOs = users.stream().map(item -> {
            UsersDTO usersDTO1 = modelMapper.map(item, UsersDTO.class);
            Optional<UserDetail> userDetail = userDetailRepository.findByUserId(item.getUserId());
            if (userDetail.isPresent()) {
                UserDetailDTO userDetailDTO = modelMapper.map(userDetail.get(), UserDetailDTO.class);
                usersDTO1.setUserDetailDTO(userDetailDTO);
            }
            return usersDTO1;
        }).collect(Collectors.toList());
        Long total = usersDTOs.stream().count();

        return new PageImpl<>(usersDTOs, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
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

    public boolean lockUser(String userId) throws Exception {
        if (!usersRepository.existsById(userId)) {
            throw new Exception("User not found with id: " + userId);
        }
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users usersExisting = users.get();
            Optional<Status> statusBanned = statusRepository.findByCodeAndType("BANNED", "USER_STATUS");
            if (statusBanned.isPresent()) {
                usersExisting.setStatusId(statusBanned.get().getStatusId());
                usersRepository.save(usersExisting);
            } else {
                throw new Exception("Status banned found");
            }
        }
        return users.isPresent();
    }

    public boolean unlockUser(String userId) throws Exception {
        if (!usersRepository.existsById(userId)) {
            throw new Exception("User not found with id: " + userId);
        }
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users usersExisting = users.get();
            Optional<Status> statusActive = statusRepository.findByCodeAndType("ACTIVE", "USER_STATUS");
            if (statusActive.isPresent()) {
                usersExisting.setStatusId(statusActive.get().getStatusId());
                usersRepository.save(usersExisting);
            } else {
                throw new Exception("Status active found");
            }
        }
        return users.isPresent();
    }

    public boolean deleteUser(String userId) throws Exception {
        if (!usersRepository.existsById(userId)) {
            throw new Exception("User not found with id: " + userId);
        }
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users usersExisting = users.get();
            usersExisting.setIsDeleted(1);
            usersRepository.save(usersExisting);
        }
        return users.isPresent();
    }

    public boolean restoreUser(String userId) throws Exception {
        if (!usersRepository.existsById(userId)) {
            throw new Exception("User not found with id: " + userId);
        }
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()) {
            Users usersExisting = users.get();
            usersExisting.setIsDeleted(0);
            usersRepository.save(usersExisting);
        }
        return users.isPresent();
    }
}
