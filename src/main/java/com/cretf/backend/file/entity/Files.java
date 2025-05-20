package com.cretf.backend.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Files")
public class Files {
    @Id
    @Column(name = "FileId")
    private String fileId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Path")
    private String path;

    @Column(name = "Type")
    private String type;
}
