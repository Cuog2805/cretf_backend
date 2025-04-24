package com.cretf.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySharedDTO {
    private String categorySharedId;

    private String code;

    private String codeParent;

    private Integer level;

    private String categoryType;

    private String name;

    private String path;

    private String component;

    private String icon;

    private String access;

    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}

