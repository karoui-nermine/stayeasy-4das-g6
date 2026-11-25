package com.stayeasy.notification.listener;

import com.stayeasy.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationEventListener {

    private final NotificationService service;

    public NotificationEventListener(NotificationService service) {
        this.service = service;
    }

    @RabbitListener(queues = "notification.queue")
    public void on(Map<String, Object> message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key) {
        service.createFromEvent(key, message);
    }
}
