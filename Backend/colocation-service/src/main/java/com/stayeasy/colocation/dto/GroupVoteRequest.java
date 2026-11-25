package com.stayeasy.colocation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GroupVoteRequest {
    private Long sharedStayId;
    private String title;
    private LocalDateTime deadline;
    private String status;
    private String options;
}
