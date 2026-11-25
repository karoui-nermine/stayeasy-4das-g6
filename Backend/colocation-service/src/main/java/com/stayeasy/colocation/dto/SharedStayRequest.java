package com.stayeasy.colocation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SharedStayRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxParticipants;
    private String experienceType;
    private Long accommodationId;
    private String creatorId;
}
