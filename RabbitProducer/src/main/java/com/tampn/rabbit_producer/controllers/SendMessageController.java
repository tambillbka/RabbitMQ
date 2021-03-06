package com.tampn.rabbit_producer.controllers;

import com.tampn.rabbit_producer.payloads.MessagePayload;
import com.tampn.rabbit_producer.services.RabbitSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/amqp")
@Slf4j
public class SendMessageController {
    private final RabbitSenderService rabbitSenderService;

    @Autowired
    public SendMessageController(RabbitSenderService rabbitSenderService) {
        this.rabbitSenderService = rabbitSenderService;
    }

    @PostMapping(value = "/producer/message")
    public ResponseEntity<Void> produceMessage(
            @RequestBody MessagePayload messagePayload
    ) {
        log.info("Message : {}", messagePayload);
        rabbitSenderService.publishMsg(messagePayload.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/producer/faker")
    public ResponseEntity<Void> produceMessage() {
        rabbitSenderService.fakeData();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
