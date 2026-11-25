package com.stayeasy.colocation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JoinRequest {
    @NotNull
    private String userId;
}
