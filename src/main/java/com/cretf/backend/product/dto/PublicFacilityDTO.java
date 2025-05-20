package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicFacilityDTO {
    private String publicFacilityId;

    private String locationId;

    private List<String> locationIds;

    private String name;

    private CoordinatesDTO coordinatesDTO;

    private Double distance;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
