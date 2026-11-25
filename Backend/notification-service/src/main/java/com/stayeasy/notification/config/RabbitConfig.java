package com.stayeasy.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "stayeasy.events";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue queue() {
        return new Queue("notification.queue", true);
    }

    @Bean
    public Binding binding0() {
        return BindingBuilder.bind(queue()).to(exchange()).with("reservation.confirmed");
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue()).to(exchange()).with("reservation.cancelled");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue()).to(exchange()).with("invoice.paid");
    }

    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(queue()).to(exchange()).with("maintenance.completed");
    }

    @Bean
    public Binding binding4() {
        return BindingBuilder.bind(queue()).to(exchange()).with("colocation.joined");
    }

    @Bean
    public Binding binding5() {
        return BindingBuilder.bind(queue()).to(exchange()).with("user.registered");
    }
}
