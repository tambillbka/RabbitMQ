package com.tampn.rabbit_producer.services;

import com.tampn.rabbit_producer.common.Helpers;
import com.tampn.rabbit_producer.common.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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

    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        assert correlationData != null;
        String messageId = correlationData.getId();
        if (ack) {
            log.info("[### RabbitSenderService ###] Sent message {} success!", messageId);
        } else {
            log.error("[### RabbitSenderService ###] Error sent message {}", messageId);
        }
    };

    public void publish(Message message, String queue) {
        // Setting confirm call back
        rabbitTemplate.setConfirmCallback(confirmCallback);

        //Sent message template
        CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getMessageId());
        rabbitTemplate.convertAndSend(exchange, queue, message, correlationData);
    }

    public void publish(Object obj, String queue) {
        Message message = MessageBuilder
                .withBody(Helpers.toByteArray(obj))
                .setMessageId(UUID.randomUUID().toString())
                .setHeader(RETRY_COUNT, 0)
                .build();
        this.publish(message, queue);
    }

    public void publishMsg(String message) {
        message = Strings.refactor(message);
        this.publish(message, msgQueue);
    }

    public void fakeData() {
        this.fake(msgQueue, 15);
        this.fake(notifyQueue, 15);
        this.fake(newsQueue, 15);
    }

    public void fake(String queue, int maxInsert) {
        String errorMsg = "[### RabbitSenderService ###] - Fake data in queue {}, Error with: {}";
        String successMsg = "[### RabbitSenderService ###] - Sent success {} message to queue - {}";

        Flux.range(0, maxInsert)
                .doOnNext(consumer -> {
                    String messageId = UUID.randomUUID().toString();
                    this.publish(messageId, queue);
                })
                .doOnError(err -> log.error(errorMsg, queue, err.getMessage()))
                .doOnComplete(() -> log.info(successMsg, maxInsert, queue))
                .subscribe();
    }

}
