package com.cretf.backend.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardDTO {
    private String dashBoardId;

    private String type;

    private String priceRangeCode;

    private String priceRange;

    private Integer priceRangeCount;

    private Double priceRangePie;

    private String name;

    private String depositContractDate;

    private Integer depositContractCount;

    private String propertyTypeName;

    private Integer propertyCount;

    private Double priceAvarage;

    private Integer totalViews;

    private Double totalDepositContractValue;

    private Integer totalPropertyActive;
}
