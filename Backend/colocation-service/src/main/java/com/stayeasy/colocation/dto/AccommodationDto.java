package com.stayeasy.colocation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/** Vue minimale d'un hebergement renvoyee par accommodation-service. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccommodationDto {
    private Long id;
    private BigDecimal pricePerNight;
}
