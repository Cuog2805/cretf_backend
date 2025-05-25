package com.cretf.backend.product.entity;

import com.cretf.backend.utils.UUIDGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UserFavourite")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavourite {
    @Id
    @GeneratedValue(generator = UUIDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = UUIDGenerator.GENERATOR_NAME, strategy = "com.cretf.backend.utils.UUIDGenerator")
    @Column(name = "UserFavouriteId")
    private String userFavouriteId;

    @Column(name = "Username")
    private String username;

    @Column(name = "PropertyId")
    private String propertyId;
}
