package com.stayeasy.accommodation.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccommodationRequest {
    private String name;
    private String description;
    private String address;
    private String city;
    private String type;
    private Integer maxCapacity;
    private BigDecimal pricePerNight;
    private String ownerId;
    private String status;
}
