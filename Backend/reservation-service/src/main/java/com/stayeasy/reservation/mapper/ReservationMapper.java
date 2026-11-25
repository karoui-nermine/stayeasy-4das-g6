package com.stayeasy.reservation.mapper;

import com.stayeasy.reservation.dto.ReservationRequest;
import com.stayeasy.reservation.dto.ReservationResponse;
import com.stayeasy.reservation.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequest r) {
        Reservation e = new Reservation();
        e.setRoomId(r.getRoomId());
        e.setGuestId(r.getGuestId());
        e.setGuestName(r.getGuestName());
        e.setOwnerId(r.getOwnerId());
        e.setCheckIn(r.getCheckIn());
        e.setCheckOut(r.getCheckOut());
        return e;
    }

    public ReservationResponse toResponse(Reservation e) {
        ReservationResponse r = new ReservationResponse();
        r.setId(e.getId());
        r.setRoomId(e.getRoomId());
        r.setGuestId(e.getGuestId());
        r.setGuestName(e.getGuestName());
        r.setOwnerId(e.getOwnerId());
        r.setCheckIn(e.getCheckIn());
        r.setCheckOut(e.getCheckOut());
        r.setStatus(e.getStatus());
        r.setTotalAmount(e.getTotalAmount());
        r.setPaymentId(e.getPaymentId());
        r.setExpiresAt(e.getExpiresAt());
        r.setCancelReason(e.getCancelReason());
        return r;
    }

    /** Charge utile des evenements RabbitMQ (reservation.requested / confirmed / cancelled). */
    public Map<String, Object> toEvent(Reservation r) {
        Map<String, Object> m = new HashMap<>();
        m.put("reservationId", r.getId());
        m.put("userId", r.getGuestId());
        m.put("ownerId", r.getOwnerId());
        m.put("customerName", r.getGuestName());
        m.put("roomLabel", "Chambre " + r.getRoomId());
        m.put("nights", ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut()));
        m.put("totalAmount", r.getTotalAmount());
        m.put("currency", "TND");
        m.put("expiresAt", String.valueOf(r.getExpiresAt()));
        return m;
    }
}
