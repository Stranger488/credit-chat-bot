package com.example.creditchatbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private MessageType messageType;
    private String sender;
    private String content;

    public enum MessageType {
        SEND,
        JOIN
    }

}
