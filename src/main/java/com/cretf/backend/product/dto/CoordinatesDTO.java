package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDTO {
    private String coordinatesId;

    private String propertyId;

    private String type;

    private String title;

    private Double latitude;

    private Double longitude;
}
