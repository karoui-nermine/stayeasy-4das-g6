package com.stayeasy.reservation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long id;
    private Long roomId;
    private String guestId;
    private String guestName;
    private String ownerId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private BigDecimal totalAmount;
    private String paymentId;
    private LocalDateTime expiresAt;
    private String cancelReason;
}
