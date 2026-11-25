package com.stayeasy.accommodation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String description;
    private String address;
    private String city;
    private String type;
    private Integer maxCapacity;
    private BigDecimal pricePerNight;
    private String ownerId;
    private String status = "ACTIVE";
}
