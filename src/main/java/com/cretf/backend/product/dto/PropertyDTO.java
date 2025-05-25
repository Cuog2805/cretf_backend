package com.cretf.backend.product.dto;

import com.cretf.backend.product.entity.PropertyPriceHistory;
import com.cretf.backend.users.dto.DepositDTO;
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

    private List<String> locationIds;

    private List<String> statusIds;

    private List<String> fileIds;

    private String propertyTypeId;

    private String propertyTypeCode;

    private String propertyTypeName;

    private String agent;

    private List<AmenityDTO> amenityDTOs;

    private List<PropertyFilesDTO> propertyFilesDTOs;

    private String priceNewest;

    private Double priceNewestValue;

    private String priceNewestScale;

    private Double priceFrom;

    private Double priceTo;

    private PropertyPriceHistoryDTO propertyPriceNewest;

    private List<PropertyPriceHistoryDTO> propertyPriceHistoryDTOs;

    private ApprovalHistoryDTO approvalHistoryDTO;

    private List<ApprovalHistoryDTO> approvalHistoryDTOs;

    private DepositDTO depositDTO;

    private CoordinatesDTO coordinatesDTO;

    private List<PublicFacilityDTO> publicFacilityDTOs;

    private String type;

    private Integer views;

    private String usernameFav;

    private int isInFavourite;

    private List<PropertyCommentDTO> propertyCommentDTOs;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
