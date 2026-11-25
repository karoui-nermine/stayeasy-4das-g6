package com.stayeasy.maintenance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaintenanceRequest {
    @NotNull
    private String title;
    private String description;
    private String priority;
    @NotNull
    private Long roomId;
    private String assignedTechnicianId;
    private String reporterId;
}
