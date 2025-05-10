package com.cretf.backend.users.entity;

import com.cretf.backend.product.entity.AuditingCreateEntity;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UserDetail")
@Data
@NoArgsConstructor
public class UserDetail {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "UserDetailId")
    private String userDetailId;

    @Column(name = "UserId")
    private String userId;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "Bio")
    private String bio;

    @Column(name = "Experience")
    private String experience;

    @Column(name = "IdentificationNumber")
    private String identificationNumber;
}
