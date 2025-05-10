package com.cretf.backend.users.entity;

import com.cretf.backend.product.entity.AuditingCreateEntity;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
public class Role extends AuditingCreateEntity {
    @Id
    @Column(name = "RoleId")
    private String roleId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;
}
