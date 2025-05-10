package com.cretf.backend.users.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositDTO {
    private String depositId;

    private String propertyId;

    private String value;

    private String scaleUnit;

    private Integer dueDate;

    private String note;
}
