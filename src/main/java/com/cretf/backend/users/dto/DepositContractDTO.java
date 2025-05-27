package com.cretf.backend.users.dto;

import com.cretf.backend.product.dto.ApprovalHistoryDTO;
import com.cretf.backend.product.entity.ApprovalHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositContractDTO {
    private String depositContractId;

    private String templateId;

    private String depositId;

    private String propertyId;

    private String fileId;

    private String fileName;

    private String seller;

    private String buyer;

    private Date dateCreated;

    private Date dueDate;

    private String downloadUrl;

    private String statusId;

    private DepositDTO depositDTO;

    private ApprovalHistoryDTO approvalHistoryDTO;

    private List<ApprovalHistoryDTO> ApprovalHistoryDTOs;
}
