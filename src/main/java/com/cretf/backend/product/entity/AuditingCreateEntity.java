package com.cretf.backend.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingCreateEntity {
    @CreatedBy
    @Column(name = "Creator")
    @JsonIgnore
    private String Creator;

    @CreatedDate
    @Column(name = "DateCreated")
    @JsonIgnore
    private Date DateCreated = new Date(System.currentTimeMillis());

    @LastModifiedBy
    @Column(name = "Modifier")
    @JsonIgnore
    private String Modifier;

    @LastModifiedDate
    @Column(name = "DateModified")
    @JsonIgnore
    private Date DateModified = new Date(System.currentTimeMillis());

    @Column(name = "IsDeleted")
    @JsonIgnore
    private int IsDeleted;
}
