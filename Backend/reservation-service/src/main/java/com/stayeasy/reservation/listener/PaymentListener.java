package com.stayeasy.reservation.listener;

import com.stayeasy.reservation.service.ReservationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentListener {

    private final ReservationService service;

    public PaymentListener(ReservationService service) {
        this.service = service;
    }

    @RabbitListener(queues = "reservation.payment")
    public void on(Map<String, Object> message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key) {
        Long reservationId = ((Number) message.get("reservationId")).longValue();
        if ("invoice.paid".equals(key)) {
            service.onPaymentSucceeded(reservationId, String.valueOf(message.get("invoiceId")));
        } else {
            service.onPaymentFailed(reservationId);
        }
    }
}
