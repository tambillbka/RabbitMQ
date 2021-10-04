package com.tampn.rabbit_producer.services;

import com.tampn.rabbit_producer.common.Helpers;
import com.tampn.rabbit_producer.common.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static com.tampn.rabbit_producer.common.Strings.RETRY_COUNT;

@Service
@Slf4j
public class RabbitSenderService {

    private final RabbitTemplate rabbitTemplate;
    @Value("${tampn.rabbitmq.message}")
    String msgQueue;
    @Value("${tampn.rabbitmq.notification}")
    String notifyQueue;
    @Value("${tampn.rabbitmq.news}")
    String newsQueue;
    @Value("${tampn.rabbitmq.exchange}")
    private String exchange;

    @Autowired
    public RabbitSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Object obj, QueueOption option) {
        Message message = MessageBuilder
                .withBody(Helpers.toByteArray(obj))
                .setMessageId(UUID.randomUUID().toString())
                .setHeader(RETRY_COUNT, 0)
                .build();

        switch (option) {
            case NEWS:
                rabbitTemplate.convertAndSend(exchange, newsQueue, message);
                break;

            case NOTIFICATION:
                rabbitTemplate.convertAndSend(exchange, notifyQueue, message);
                break;

            case MESSAGE:
            default:
                rabbitTemplate.convertAndSend(exchange, msgQueue, message);
                break;
        }
    }

    public void publishMsg(String message) {
        message = Strings.refactor(message);
        this.publish(message, QueueOption.MESSAGE);

        Flux.range(0, 12)
                .doOnNext(consumer -> {
                    String messageId = UUID.randomUUID().toString();
                    this.publish(messageId, QueueOption.MESSAGE);
                })
                .doOnError(err -> log.error("Has an Error!"))
                .doOnComplete(() -> log.info("Complete!"))
                .subscribe();

        Flux.range(0, 12)
                .doOnNext(consumer -> {
                    String messageId = UUID.randomUUID().toString();
                    this.publish(messageId, QueueOption.NOTIFICATION);
                })
                .doOnError(err -> log.error("Has an Error!"))
                .doOnComplete(() -> log.info("Complete!"))
                .subscribe();

        Flux.range(0, 20)
                .doOnNext(consumer -> {
                    String messageId = UUID.randomUUID().toString();
                    this.publish(messageId, QueueOption.NEWS);
                })
                .doOnError(err -> log.error("Has an Error!"))
                .doOnComplete(() -> log.info("Complete!"))
                .subscribe();
    }

}
