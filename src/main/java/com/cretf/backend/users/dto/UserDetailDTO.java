package com.cretf.backend.users.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
    private String userDetailId;

    private String userId;

    private String phone;

    private String fullName;

    private String gender;

    private String avatar;

    private String bio;

    private String experience;

    private String identificationNumber;
}
