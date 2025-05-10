package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPriceHistoryDTO {
    private String propertyPriceHistoryId;

    private String propertyId;

    private Double value;

    private String scaleId;

    private String scaleUnit;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
