package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "Property")
@Data
@NoArgsConstructor
public class Property extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyId")
    private String PropertyId;

    @Column(name = "Code")
    private String Code;

    @Column(name = "Name")
    private String Name;

    @Column(name = "AddressSpecific")
    private String AddressSpecific;

    @Column(name = "BuildIn")
    private Date BuildIn;

    @Column(name = "LocationId")
    private String LocationId;

    @Column(name = "StatusId")
    private String StatusId;

    @Column(name = "PropertyTypeId")
    private String PropertyTypeId;
}
