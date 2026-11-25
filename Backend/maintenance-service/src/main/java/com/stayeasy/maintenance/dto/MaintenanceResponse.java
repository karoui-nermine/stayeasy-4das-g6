package com.stayeasy.maintenance.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime reportDate;
    private String status;
    private String priority;
    private Long roomId;
    private String assignedTechnicianId;
    private String reporterId;
    private Boolean roomSyncPending;
}
