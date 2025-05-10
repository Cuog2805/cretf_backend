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

    public String propertyAmenityId;

    public String propertyId;

    private String code;

    private String name;

    private String description;

    private String amenityType;

    private String amenityTypeName;

    private Boolean isGeneralInfo;

    public Integer value;

    public String scaleId;

    public String scaleUnit;

    public String valueDisplay;

    public String level;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
