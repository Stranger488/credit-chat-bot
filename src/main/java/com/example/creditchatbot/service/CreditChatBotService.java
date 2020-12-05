package com.example.creditchatbot.service;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.util.ChatBot;
import com.example.creditchatbot.util.MessageBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CreditChatBotService {
    private final MessageBuilder messageBuilder;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final HashMap<String, String> users = new HashMap<>();
    private final HashMap<String, ChatBot> chatBots = new HashMap<>();

    public CreditChatBotService(MessageBuilder messageBuilder, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageBuilder = messageBuilder;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addUserAndSendWelcome(String userUniqueName, ChatMessage chatMessage) {
        users.put(userUniqueName, userUniqueName);
        chatBots.put(userUniqueName, new ChatBot());

        ChatBot chatBot = chatBots.get(userUniqueName);

        chatMessage.setMessageType(ChatMessage.MessageType.JOIN);
        chatMessage.setSender("server");
        chatMessage.setContent(chatBot.getCurrentMessage(messageBuilder));

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", chatMessage);
    }

    public void sendMsgToUser(String userName, ChatMessage msg) {
        msg.setMessageType(ChatMessage.MessageType.SEND);
        String user = users.get(userName);

        simpMessagingTemplate.convertAndSendToUser(user, "/channel", msg);
    }


    public void sendBotMsgToUser(String userName, ChatMessage msg) {
        String user = users.get(userName);
        ChatBot chatBot = chatBots.get(userName);

        chatBot.processMsg(msg);

        ChatMessage newMsg = new ChatMessage();
        newMsg.setSender("server");
        newMsg.setMessageType(ChatMessage.MessageType.SEND);
        newMsg.setContent(chatBot.getCurrentMessage(messageBuilder));

        simpMessagingTemplate.convertAndSendToUser(user, "/channel", newMsg);
    }
}
