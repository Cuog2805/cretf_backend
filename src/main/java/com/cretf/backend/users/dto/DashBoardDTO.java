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

    private String title;

    private String category;

    private Integer sortOrder;

    private Double value;

    private String scaleUnit;

    private Double avgValue;

    private Double totalValue;

    private Double count;

    private Double percentage;

    private Double minValue;

    private Double maxValue;
}
