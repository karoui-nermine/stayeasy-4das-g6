package com.stayeasy.colocation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SharedStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxParticipants;
    private Integer currentParticipants = 0;
    private String experienceType;
    private String status = "OPEN";
    private Long accommodationId;
    private String creatorId;
}
