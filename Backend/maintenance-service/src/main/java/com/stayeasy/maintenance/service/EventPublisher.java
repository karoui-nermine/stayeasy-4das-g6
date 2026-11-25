package com.stayeasy.maintenance.service;

import com.stayeasy.maintenance.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Publie les événements métier sur l'exchange RabbitMQ commun. */
@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, routingKey, payload);
    }
}
