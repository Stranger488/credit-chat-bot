package com.example.creditchatbot.model;

import java.util.Objects;

public class ChatMessage {
    private MessageType messageType;
    private String sender;
    private String content;

    public enum MessageType {
        SEND,
        JOIN
    }

    public ChatMessage() {
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return messageType == that.messageType &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, sender, content);
    }
}
