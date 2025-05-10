package com.cretf.backend.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositContractDTO {
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

    private DepositDTO depositDTO;
}
