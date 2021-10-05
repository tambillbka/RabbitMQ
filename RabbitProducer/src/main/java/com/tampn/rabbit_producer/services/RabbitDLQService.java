package com.tampn.rabbit_producer.services;

import com.tampn.rabbit_producer.common.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.tampn.rabbit_producer.common.Strings.RETRY_COUNT;

@Service
@Slf4j
public class RabbitDLQService {

    private final RabbitSenderService senderService;
    @Value("${tampn.rabbitmq.max.retry}")
    int maxRetry;
    @Value("${tampn.rabbitmq.plq}")
    String plqQueue;

    @Autowired
    public RabbitDLQService(RabbitSenderService senderService) {
        this.senderService = senderService;
    }

    @RabbitListener(queues = "${tampn.rabbitmq.dlq}")
    public void processMessageDLQ(Message message) {
        if (Objects.isNull(message)) {
            return;
        }
        Integer retryCount = message.getMessageProperties().getHeader(RETRY_COUNT);
        retryCount = Objects.isNull(retryCount) ? 0 : retryCount;

        String queueRetry = message.getMessageProperties().getHeader("x-first-death-queue");
        log.info("Message: {}", Helpers.toObject(message.getBody()));

        if (retryCount >= maxRetry) {
            log.info("[### RabbitDLQService ###] Push message {} to Parking lot queue.", message.getMessageProperties().getMessageId());
            senderService.publish(message, plqQueue);
            return;
        }

        message.getMessageProperties().getHeaders().put(RETRY_COUNT, retryCount + 1);
        CompletableFuture.runAsync(
                () -> senderService.publish(message, queueRetry),
                CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS)
        );
    }

}
