package com.stayeasy.facturation.listener;

import com.stayeasy.facturation.service.InvoiceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReservationListener {

    private final InvoiceService service;

    public ReservationListener(InvoiceService service) {
        this.service = service;
    }

    @RabbitListener(queues = "facturation.queue")
    public void on(Map<String, Object> message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key) {
        Long reservationId = ((Number) message.get("reservationId")).longValue();
        if ("reservation.requested".equals(key)) {
            service.onReservationRequested(reservationId, message);
        } else {
            service.onReservationCancelled(reservationId);
        }
    }
}
