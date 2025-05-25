package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmenityDTO {
    private String amenityId;

    private String propertyAmenityId;

    private String propertyId;

    private String code;

    private String name;

    private String description;

    private String amenityType;

    private String amenityTypeName;

    private Boolean isGeneralInfo;

    private Integer value;

    private String scaleId;

    private String scaleUnit;

    private String valueDisplay;

    private String level;

    private Integer ordinal;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
