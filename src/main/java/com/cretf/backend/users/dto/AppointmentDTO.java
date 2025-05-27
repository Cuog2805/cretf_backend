package com.cretf.backend.users.dto;

import com.cretf.backend.product.dto.ApprovalHistoryDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private String appointmentId;

    private String propertyId;

    private String propertyAddress;

    private String buyerId;

    private String buyer;

    private String sellerId;

    private String seller;

    private String agentId;

    private String agent;

    private String type;

    private Date date;

    private String statusId;

    private String note;

    private ApprovalHistoryDTO approvalHistoryDTO;

    private List<ApprovalHistoryDTO> approvalHistoryDTOs;
    //
    private String creator;

    private Date dateCreated;

    private String modifier;

    private Date dateModified;

    private int isDeleted;
}
