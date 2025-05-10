package com.cretf.backend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyAmenityDTO {
    public String propertyAmenityId;

    public String propertyId;

    public String amenityId;

    public Integer value;

    public String level;
}
