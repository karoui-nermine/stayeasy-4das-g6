package com.stayeasy.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private String guestId;
    private String guestName;
    private String ownerId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status = "PENDING";
    private BigDecimal totalAmount;
    private String paymentId;
    private LocalDateTime expiresAt;
    private String cancelReason;
}
