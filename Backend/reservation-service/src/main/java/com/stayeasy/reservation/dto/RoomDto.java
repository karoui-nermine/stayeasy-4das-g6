package com.stayeasy.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/** Vue minimale d'une chambre renvoyee par accommodation-service. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDto {
    private Long id;
    private String status;
    private BigDecimal price;
}
