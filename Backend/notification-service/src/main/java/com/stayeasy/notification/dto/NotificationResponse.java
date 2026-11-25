package com.stayeasy.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String userId;
    private String type;
    private String title;
    private String message;
    private String channel;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
