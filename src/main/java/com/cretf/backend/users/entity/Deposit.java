package com.cretf.backend.users.entity;

import com.cretf.backend.product.entity.AuditingCreateEntity;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "Deposit")
@Data
@NoArgsConstructor
public class Deposit {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "DepositId")
    private String depositId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "Value")
    private String value;

    @Column(name = "ScaleUnit")
    private String scaleUnit;

    @Column(name = "DueDate")
    private Integer dueDate;

    @Column(name = "Note")
    private String note;
}
