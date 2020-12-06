package com.example.creditchatbot.service;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.util.ChatBot;
import com.example.creditchatbot.util.MessageBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Objects;

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

    public void addUserAndSendWelcome(String userUniqueName) {
        ChatBot chatBot = chatBots.get(userUniqueName);

        String responseWelcomeString = chatBot.getCurrentMessage(messageBuilder);

        ChatMessage responseMsg = new ChatMessage();
        responseMsg.setSender("server");
        responseMsg.setMessageType(ChatMessage.MessageType.JOIN);
        responseMsg.setContent(responseWelcomeString);

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", responseMsg);


        chatBot.nextState(); // Switch from JOIN to INIT
        String responseInitString = chatBot.getCurrentMessage(messageBuilder);

        responseMsg.setMessageType(ChatMessage.MessageType.SEND);
        responseMsg.setContent(responseInitString);

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", responseMsg);


        chatBot.nextState(); // Switch from INIT to IS_BANK_CLIENT
        String responseFirstString = chatBot.getCurrentMessage(messageBuilder);

        responseMsg.setContent(responseFirstString);

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", responseMsg);
    }

    public void sendUserMsgToChannel(String userName, ChatMessage msg) {
        msg.setMessageType(ChatMessage.MessageType.SEND);
        String user = users.get(userName);

        simpMessagingTemplate.convertAndSendToUser(user, "/channel", msg);
    }


    public void sendBotMsgToUser(String userName, ChatMessage msg) {
        String user = users.get(userName);
        ChatBot chatBot = chatBots.get(userName);

        String responseString = chatBot.processMsg(msg, messageBuilder);

        ChatMessage responseMsg = new ChatMessage();
        responseMsg.setSender("server");
        responseMsg.setMessageType(ChatMessage.MessageType.SEND);
        responseMsg.setContent(responseString);

        simpMessagingTemplate.convertAndSendToUser(user, "/channel", responseMsg);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = Objects.requireNonNull(headerAccessor.getUser()).getName();

        users.put(username, username);
        chatBots.put(username, new ChatBot());

        System.out.println("Websocket connection established  " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = Objects.requireNonNull(headerAccessor.getUser()).getName();

        System.out.println("User Disconnected : " + username);

        users.remove(username);
        chatBots.remove(username);
    }
}
