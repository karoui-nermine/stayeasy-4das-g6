package com.stayeasy.facturation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Long reservationId;
    private String userId;
    private String ownerId;
    private String customerName;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentUrl;
    private String status;
    private LocalDateTime expiresAt;
    private LocalDateTime paidAt;
}
