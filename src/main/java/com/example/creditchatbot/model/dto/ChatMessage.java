package com.example.creditchatbot.model.dto;

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

    public ChatMessage(String sender, String content, MessageType messageType) {
        this.messageType = messageType;
        this.sender = sender;
        this.content = content;
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
}
