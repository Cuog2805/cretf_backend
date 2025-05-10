package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Data
@NoArgsConstructor
@Table(name = "PropertyAmenity")
public class PropertyAmenity{
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyAmenityId")
    private String propertyAmenityId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "AmenityId")
    private String amenityId;

    @Column(name = "Value")
    private String value;

    @Column(name = "Level")
    private String level;
}
