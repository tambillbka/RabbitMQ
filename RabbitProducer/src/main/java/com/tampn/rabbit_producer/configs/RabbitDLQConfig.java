package com.tampn.rabbit_producer.configs;

import com.tampn.rabbit_producer.configs.annotations.DeadMessage;
import com.tampn.rabbit_producer.configs.annotations.RabbitMessage;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDLQConfig {
    @Value("${tampn.rabbitmq.exchange}")
    String exchange;

    @Value("${tampn.rabbitmq.dlq}")
    String dlqQueue;

    @Value("${tampn.rabbitmq.dlq.maxlength}")
    private int maxLength;

    @Bean
    @DeadMessage
    Queue dlqQueue() {
        return QueueBuilder
                .durable(dlqQueue)
                .maxLength(maxLength)
                .build();
    }

    @Bean
    @DeadMessage
    Binding bindingBuilder(
            @RabbitMessage Exchange exchange,
            @DeadMessage Queue queue
    ) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(dlqQueue)
                .noargs();
    }
}
