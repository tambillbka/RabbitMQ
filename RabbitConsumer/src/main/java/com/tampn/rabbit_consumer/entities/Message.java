package com.tampn.rabbit_consumer.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.UUIDGenerator.class,
        scope = Message.class
)
public class Message {
    private String content;
    private Timestamp sendTime;
    private MessageType messageType;
}
