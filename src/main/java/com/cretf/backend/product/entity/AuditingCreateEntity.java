package com.cretf.backend.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingCreateEntity {
    @CreatedBy
    @Column(name = "Creator")
    @JsonIgnore
    private String creator;

    @CreatedDate
    @Column(name = "DateCreated")
    @JsonIgnore
    private Date dateCreated = new Date(System.currentTimeMillis());

    @LastModifiedBy
    @Column(name = "Modifier")
    @JsonIgnore
    private String modifier;

    @LastModifiedDate
    @Column(name = "DateModified")
    @JsonIgnore
    private Date dateModified = new Date(System.currentTimeMillis());

    @Column(name = "IsDeleted")
    @JsonIgnore
    private int isDeleted;
}
