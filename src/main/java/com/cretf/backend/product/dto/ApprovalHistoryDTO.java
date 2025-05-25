package com.cretf.backend.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalHistoryDTO {
    private String approvalId;

    private String propertyId;

    private String statusId;

    private String note;

    private String approvalDate;

    private String approver;
}
