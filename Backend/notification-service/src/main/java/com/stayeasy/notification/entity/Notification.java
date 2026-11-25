package com.stayeasy.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String type;
    private String title;
    @Column(length = 1000)
    private String message;
    private String channel = "IN_APP";
    private String status = "SENT";
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
