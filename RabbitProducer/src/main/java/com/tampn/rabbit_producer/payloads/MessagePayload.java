package com.tampn.rabbit_producer.payloads;

import com.tampn.rabbit_producer.common.Strings;
import com.tampn.rabbit_producer.entities.Message;
import com.tampn.rabbit_producer.entities.MessageType;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class MessagePayload {
    private String content;
    private boolean privateMessage;

    public Message toMessage() {
        Message message = new Message();
        message.setMessageType(
                this.privateMessage
                        ? MessageType.PRIVATE
                        : MessageType.PUBLIC
        );
        message.setSendTime(new Timestamp(System.currentTimeMillis()));
        message.setContent(Strings.refactor(this.content));
        return message;
    }
}