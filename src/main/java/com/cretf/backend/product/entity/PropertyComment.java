package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PropertyComment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyComment extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyCommentId")
    private String propertyCommentId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "ParentCode")
    private String parentCode;

    @Column(name = "Code")
    private String code;

    @Column(name = "Path")
    private String path;

    @Column(name = "Level")
    private Integer level;

    @Column(name = "Content")
    private String content;
}
