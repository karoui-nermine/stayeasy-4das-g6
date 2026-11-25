package com.stayeasy.reservation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationUpdateRequest {
    private LocalDate checkIn;
    private LocalDate checkOut;
}
