package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Scale")
@Data
@NoArgsConstructor
public class Scale extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "ScaleId")
    private String scaleId;

    @Column(name = "Code")
    private String code;

    @Column(name = "Unit")
    private String unit;

    @Column(name = "Description")
    private String description;

    @Column(name = "Type")
    private String type;
}
