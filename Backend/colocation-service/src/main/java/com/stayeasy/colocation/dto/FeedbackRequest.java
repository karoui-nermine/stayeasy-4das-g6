package com.stayeasy.colocation.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long sharedStayId;
    private String userId;
    private Integer rating;
    private String comment;
}
