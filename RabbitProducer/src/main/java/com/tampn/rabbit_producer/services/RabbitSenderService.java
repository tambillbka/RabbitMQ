package com.tampn.rabbit_producer.services;

import com.tampn.rabbit_producer.entities.Message;
import com.tampn.rabbit_producer.entities.MessageType;
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

    private void producePrivateMessage(Message message) {
        amqpTemplate.convertAndSend(exchange, routingKey, message);
        log.info("Produce private message: {}", message);
    }

    private void producePublicMessage(Message message) {
        amqpTemplate.convertAndSend(message);
        log.info("Produce public message: {}", message);
    }

    public void produceMessageWithType(Message message) {
        if (message.getMessageType().equals(MessageType.PRIVATE)) {
            this.producePrivateMessage(message);
        } else {
            this.producePublicMessage(message);
        }
    }
}
