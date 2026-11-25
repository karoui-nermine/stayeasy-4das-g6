package com.stayeasy.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    @NotNull
    private Long roomId;
    @NotNull
    private String guestId;
    private String guestName;
    private String ownerId;
    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate checkOut;
}
