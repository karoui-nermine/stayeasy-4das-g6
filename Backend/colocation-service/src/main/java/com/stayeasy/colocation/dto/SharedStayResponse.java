package com.stayeasy.colocation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SharedStayResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String experienceType;
    private String status;
    private Long accommodationId;
    private String creatorId;
}
