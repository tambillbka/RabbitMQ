package com.tampn.rabbit_producer.configs;

import com.tampn.rabbit_producer.configs.annotations.RabbitMessage;
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
public class RabbitConfig {

    @Value("${tampn.rabbitmq.exchange}")
    String exchange;

    @Value("${tampn.rabbitmq.message}")
    String msgQueue;

    @Value("${tampn.rabbitmq.notification}")
    String notifyQueue;

    @Value("${tampn.rabbitmq.news}")
    String newsQueue;

    @Value("${tampn.rabbitmq.dlq}")
    String dlqQueue;

    @Value("${tampn.rabbit.msg.maxlength}")
    int maxLength;

    @Value("${tampn.rabbitmq.ttl}")
    int timeToLive;

    @Bean
    @RabbitMessage
    Exchange exchange() {
        return ExchangeBuilder
                .directExchange(exchange)
                .build();
    }

    @Bean
    @RabbitMessage
    Queue msgQueue() {
        return QueueBuilder
                .durable(msgQueue)
                .maxLength(maxLength)
                .ttl(timeToLive)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(dlqQueue)
                .build();
    }

    @Bean
    @RabbitMessage
    Binding bindingMsgQueue() {
        return BindingBuilder
                .bind(this.msgQueue())
                .to(this.exchange())
                .with(msgQueue)
                .noargs();
    }

    @Bean
    @RabbitMessage
    Queue notifyQueue() {
        return QueueBuilder
                .durable(notifyQueue)
                .maxLength(maxLength)
                .ttl(timeToLive)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(dlqQueue)
                .build();
    }

    @Bean
    @RabbitMessage
    Binding bindingNotifyQueue() {
        return BindingBuilder
                .bind(this.notifyQueue())
                .to(this.exchange())
                .with(notifyQueue)
                .noargs();
    }

    @Bean
    @RabbitMessage
    Queue newsQueue() {
        return QueueBuilder
                .durable(newsQueue)
                .maxLength(maxLength)
                .ttl(timeToLive)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(dlqQueue)
                .build();
    }

    @Bean
    @RabbitMessage
    Binding bindingNewsQueue() {
        return BindingBuilder
                .bind(this.newsQueue())
                .to(this.exchange())
                .with(newsQueue)
                .noargs();
    }
}
