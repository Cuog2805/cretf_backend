package com.cretf.backend.users.entity;

import com.cretf.backend.product.entity.AuditingCreateEntity;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "Appointment")
@Data
@NoArgsConstructor
public class Appointment extends AuditingCreateEntity {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "AppointmentId")
    private String appointmentId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "BuyerId")
    private String buyerId;

    @Column(name = "SellerId")
    private String sellerId;

    @Column(name = "AgentId")
    private String agentId;

    @Column(name = "Type")
    private String type;

    @Column(name = "Date")
    private Date date;

    @Column(name = "StatusId")
    private String statusId;

    @Column(name = "Note")
    private String note;
}
