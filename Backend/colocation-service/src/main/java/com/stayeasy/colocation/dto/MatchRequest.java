package com.stayeasy.colocation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MatchRequest {
    @NotNull
    private Long sharedStayId;
    private List<String> preferences;
    private Double budgetMax;
}
