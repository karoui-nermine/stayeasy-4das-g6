package com.stayeasy.accommodation.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomRequest {
    private String number;
    private String type;
    private Integer capacity;
    private BigDecimal price;
    private String status;
    private Long accommodationId;
}
