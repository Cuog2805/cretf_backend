package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "ApprovalHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistory {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "ApprovalId")
    private String approvalId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "StatusId")
    private String statusId;

    @Column(name = "Note")
    private String note;

    @Column(name = "ApprovalDate")
    private Date approvalDate;

    @Column(name = "Approver")
    private String approver;
}
