package com.cretf.backend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private String propertyId;

    private String code;

    private String name;

    private String addressSpecific;

    private Date buildIn;

    private String locationId;

    private String statusId;

    private String propertyTypeId;

    private String propertyTypeCode;

    private String propertyTypeName;

    //audit
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
