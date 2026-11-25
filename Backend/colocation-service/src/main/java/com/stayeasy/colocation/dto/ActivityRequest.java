package com.stayeasy.colocation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityRequest {
    private Long sharedStayId;
    private String title;
    private LocalDateTime activityDate;
    private String location;
}
