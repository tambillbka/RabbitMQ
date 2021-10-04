package com.tampn.rabbit_producer.payloads;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessagePayload {
    private String message;
}
