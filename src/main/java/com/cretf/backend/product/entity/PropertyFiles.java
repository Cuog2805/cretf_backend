package com.cretf.backend.product.entity;

import com.cretf.backend.converter.JsonListConverter;
import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "PropertyFiles")
@Data
@NoArgsConstructor
public class PropertyFiles {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "PropertyFilesId")
    private String propertyFilesId;

    @Column(name = "PropertyId")
    private String propertyId;

    @Column(name = "FileId")
    @Convert(converter = JsonListConverter.class)
    private List<String> fileIds;

    @Column(name = "Category")
    private String category;
}
