package com.stayeasy.facturation.mapper;

import com.stayeasy.facturation.dto.InvoiceResponse;
import com.stayeasy.facturation.entity.Invoice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Map;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice e) {
        InvoiceResponse r = new InvoiceResponse();
        r.setId(e.getId());
        r.setInvoiceNumber(e.getInvoiceNumber());
        r.setReservationId(e.getReservationId());
        r.setUserId(e.getUserId());
        r.setOwnerId(e.getOwnerId());
        r.setCustomerName(e.getCustomerName());
        r.setTotalAmount(e.getTotalAmount());
        r.setCurrency(e.getCurrency());
        r.setPaymentUrl(e.getPaymentUrl());
        r.setStatus(e.getStatus());
        r.setExpiresAt(e.getExpiresAt());
        r.setPaidAt(e.getPaidAt());
        return r;
    }

    /** Construit une facture a partir de l'evenement reservation.requested. */
    public Invoice fromReservationEvent(Long reservationId, Map<String, Object> m) {
        Invoice i = new Invoice();
        i.setInvoiceNumber("FAC-" + Year.now() + "-" + String.format("%04d", reservationId));
        i.setReservationId(reservationId);
        i.setUserId(String.valueOf(m.get("userId")));
        i.setOwnerId(String.valueOf(m.get("ownerId")));
        i.setCustomerName(String.valueOf(m.get("customerName")));
        i.setTotalAmount(new BigDecimal(String.valueOf(m.get("totalAmount"))));
        i.setExpiresAt(LocalDateTime.parse(String.valueOf(m.get("expiresAt"))));
        i.setPaymentUrl("http://localhost:3000/#pay-" + i.getInvoiceNumber());
        return i;
    }
}
