package com.cretf.backend.product.entity;

import com.cretf.backend.converter.JsonListConverter;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Property")
@Data
@NoArgsConstructor
public class Property extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "Code")
    private String code;

    @Column(name = "Name")
    private String name;

    @Column(name = "AddressSpecific")
    private String addressSpecific;

    @Column(name = "BuildIn")
    private Date buildIn;

    @Column(name = "LocationId")
    private String locationId;

    @Column(name = "StatusId")
    @Convert(converter = JsonListConverter.class)
    private List<String> statusIds;

    @Column(name = "FileId")
    @Convert(converter = JsonListConverter.class)
    private List<String> fileIds;

    @Column(name = "PropertyTypeId")
    private String propertyTypeId;

    @Column(name = "Agent")
    private String agents;

    @Column(name = "Type")
    private String type;
}
