package com.tampn.rabbit_producer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitProducerApplication.class, args);
    }

}
