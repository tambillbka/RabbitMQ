package com.tampn.rabbit_producer.services;

import com.tampn.rabbit_producer.entities.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitSenderService {

    private final AmqpTemplate amqpTemplate;
    @Value("${tampn.rabbitmq.exchange}")
    private String exchange;
    @Value("${tampn.rabbitmq.routing}")
    private String routingKey;

    @Autowired
    public RabbitSenderService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void produceMessage(Message message) {
        amqpTemplate.convertAndSend(exchange, routingKey, message);
        log.info("Produce private message: {}", message);
    }
}
