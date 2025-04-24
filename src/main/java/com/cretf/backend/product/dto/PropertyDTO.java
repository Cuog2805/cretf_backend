package com.cretf.backend.product.dto;

import com.cretf.backend.product.entity.PropertyPriceHistory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    private List<String> statusIds;

    private List<String> fileIds;

    private String propertyTypeId;

    private String propertyTypeCode;

    private String propertyTypeName;

    private List<AmenityDTO> amenityDTOs;

    private List<PropertyFilesDTO> propertyFilesDTOs;

    private PropertyPriceHistoryDTO propertyPriceNewest;
    private List<PropertyPriceHistoryDTO> propertyPriceHistoryDTOs;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
