package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PropertyNearbyPlace")
@Data
@NoArgsConstructor
public class PropertyNearbyPlace {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyNearbyPlaceId")
    private String propertyNearbyPlaceId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "PublicFacilityId")
    private String publicFacilityId;

    @Column(name = "Distance")
    private Double distance;
}
