package com.stayeasy.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private LocalDateTime reportDate;
    private String status = "OPEN";
    private String priority = "MEDIUM";
    private Long roomId;
    private String assignedTechnicianId;
    private String reporterId;
    private Boolean roomSyncPending = false;
}
