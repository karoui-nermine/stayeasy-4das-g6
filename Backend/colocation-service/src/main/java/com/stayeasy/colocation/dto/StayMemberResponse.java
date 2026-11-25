package com.stayeasy.colocation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StayMemberResponse {
    private Long id;
    private Long sharedStayId;
    private String userId;
    private String role;
    private String membershipStatus;
    private LocalDate joinDate;
}
