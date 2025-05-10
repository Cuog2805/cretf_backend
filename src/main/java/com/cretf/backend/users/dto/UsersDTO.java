package com.cretf.backend.users.dto;

import com.cretf.backend.users.entity.Role;
import com.cretf.backend.users.entity.UserDetail;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private String userId;

    private String username;

    private String email;

    private String password;

    private String newPassword;

    private String roleId;

    private String statusId;

    private String locationId;

    private String token;

    private UserDetailDTO userDetailDTO;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
