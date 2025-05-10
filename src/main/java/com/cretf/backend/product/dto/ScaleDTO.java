package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScaleDTO {
    private String scaleId;

    private String code;

    private String unit;

    private String description;

    private String type;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
