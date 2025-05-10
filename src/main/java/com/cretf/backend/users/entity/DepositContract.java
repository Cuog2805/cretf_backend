package com.cretf.backend.users.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "DepositContract")
@Data
@NoArgsConstructor
public class DepositContract {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "depositContractId")
    private String DepositContractId;

    @Column(name = "DepositId")
    private String depositId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "FileId")
    private String fileId;

    @Column(name = "FileName")
    private String fileName;

    @Column(name = "Seller")
    private String seller;

    @Column(name = "Buyer")
    private String buyer;

    @Column(name = "DateCreated")
    private Date dateCreated;

    @Column(name = "DueDate")
    private Date dueDate;

    @Column(name = "DownloadUrl")
    private String downloadUrl;
}
