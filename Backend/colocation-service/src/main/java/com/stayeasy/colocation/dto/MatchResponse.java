package com.stayeasy.colocation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long sharedStayId;
    private int score;
}
