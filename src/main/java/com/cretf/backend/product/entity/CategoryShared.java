package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Data
@Table(name = "CategoryShared")
@NoArgsConstructor
public class CategoryShared extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "CategorySharedId")
    private String categorySharedId;

    @Column(name = "Code")
    private String code;

    @Column(name = "CodeParent")
    private String codeParent;

    @Column(name = "Level")
    private Integer level;

    @Column(name = "CategoryType")
    private String categoryType;

    @Column(name = "Name")
    private String name;

    @Column(name = "Path")
    private String path;

    @Column(name = "Component")
    private String component;

    @Column(name = "Icon")
    private String icon;

    @Column(name = "Access")
    private String access;
}
