package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private String locationId;

    private String parentCode;

    private String code;

    private String path;

    private int level;

    private String name;

    private String fullname;

    private String description;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
