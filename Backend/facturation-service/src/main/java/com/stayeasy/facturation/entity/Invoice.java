package com.stayeasy.facturation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private Long reservationId;
    private String userId;
    private String ownerId;
    private String customerName;
    private BigDecimal totalAmount;
    private String currency = "TND";
    private String paymentUrl;
    private String status = "PENDING";
    private LocalDateTime expiresAt;
    private LocalDateTime paidAt;
}
