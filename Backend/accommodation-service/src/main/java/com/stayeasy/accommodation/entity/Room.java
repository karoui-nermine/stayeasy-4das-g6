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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String type;
    private Integer capacity;
    private BigDecimal price;
    private String status = "DISPONIBLE";
    private Long accommodationId;
}
