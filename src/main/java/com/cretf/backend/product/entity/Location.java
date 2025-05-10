package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Location")
@Data
@NoArgsConstructor
public class Location extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "LocationId")
    private String locationId;

    @Column(name = "ParentCode")
    private String parentCode;

    @Column(name = "Code")
    private String code;

    @Column(name = "Path")
    private String path;

    @Column(name = "Level")
    private Integer level;

    @Column(name = "Name")
    private String name;

    @Column(name = "description")
    private String description;
}
