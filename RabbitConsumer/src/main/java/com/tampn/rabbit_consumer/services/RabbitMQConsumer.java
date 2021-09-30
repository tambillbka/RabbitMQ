package com.tampn.rabbit_consumer.services;

import com.tampn.rabbit_consumer.entities.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "${tampn.rabbitmq.queue}")
    public void consumeMessage(Message message) {
        log.info("Received Message: {}", message);
    }
}
