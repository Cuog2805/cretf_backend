package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PropertyType")
@Data
@NoArgsConstructor
public class PropertyType extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyTypeId")
    private String propertyTypeId;

    @Column(name = "Code")
    private String code;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;
}
