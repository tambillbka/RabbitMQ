package com.tampn.rabbit_producer.configs;

import com.tampn.rabbit_producer.configs.annotations.ParkingLotQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitPLQConfig {
    @Value("${tampn.rabbitmq.exchange}")
    String exchange;

    @Value("${tampn.rabbitmq.plq}")
    String plqQueue;

    @Value("${tampn.rabbitmq.plq.maxlength}")
    private int maxLength;

    @Bean
    @ParkingLotQueue
    Exchange plqExchange() {
        return ExchangeBuilder
                .directExchange(exchange)
                .build();
    }

    @Bean
    @ParkingLotQueue
    Queue plqQueue() {
        return QueueBuilder
                .durable(plqQueue)
                .maxLength(maxLength)
                .build();
    }

    @Bean
    @ParkingLotQueue
    Binding pqlBindingBuilder(
            @ParkingLotQueue Exchange exchange,
            @ParkingLotQueue Queue queue
    ) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(String.valueOf(queue))
                .noargs();
    }
}
