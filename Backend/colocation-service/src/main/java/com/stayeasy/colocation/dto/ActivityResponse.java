package com.stayeasy.colocation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityResponse {
    private Long id;
    private Long sharedStayId;
    private String title;
    private LocalDateTime activityDate;
    private String location;
}
