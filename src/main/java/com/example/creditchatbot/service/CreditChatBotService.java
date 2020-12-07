package com.example.creditchatbot.service;

import com.example.creditchatbot.model.Client;
import com.example.creditchatbot.model.dto.ChatMessage;
import com.example.creditchatbot.repository.ClientRepository;
import com.example.creditchatbot.util.ChatBot;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Objects;

@Service
public class CreditChatBotService {

    private final ClientRepository clientRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final HashMap<String, ChatBot> chatBots = new HashMap<>();

    public CreditChatBotService(SimpMessagingTemplate simpMessagingTemplate, ClientRepository clientRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.clientRepository = clientRepository;
    }

    public void addUserAndSendWelcome(String userUniqueName) {
        ChatBot chatBot = chatBots.get(userUniqueName);

        String responseWelcomeString = chatBot.getCurrentMessage();

        ChatMessage responseMsg = new ChatMessage();
        responseMsg.setSender("server");
        responseMsg.setMessageType(ChatMessage.MessageType.JOIN);
        responseMsg.setContent(responseWelcomeString);

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", responseMsg);


        chatBot.nextState(); // Switch from JOIN to INIT
        String responseInitString = chatBot.getCurrentMessage();

        chatBot.nextState(); // Switch from INIT to CITY
        String responseFirstString = chatBot.getCurrentMessage();

        responseMsg.setMessageType(ChatMessage.MessageType.SEND);
        responseMsg.setContent(responseInitString + responseFirstString);

        simpMessagingTemplate.convertAndSendToUser(userUniqueName, "/channel", responseMsg);
    }

    public void sendUserMsgToChannel(String userName, ChatMessage msg) {
        msg.setMessageType(ChatMessage.MessageType.SEND);

        simpMessagingTemplate.convertAndSendToUser(userName, "/channel", msg);
    }

    public void sendBotMsgToUser(String userName, ChatMessage userMsg) {
        Client client = Objects.requireNonNull(clientRepository.findByGeneratedUniqueName(userName));
        ChatBot chatBot = chatBots.get(userName);

        String responseString = chatBot.processMsg(userMsg, client);

        ChatMessage responseMsg = new ChatMessage();
        responseMsg.setSender("server");
        responseMsg.setMessageType(ChatMessage.MessageType.SEND);
        responseMsg.setContent(responseString);

        if (client.getGeneratedUniqueName() == null) { // DECLINE or END_SESSION state, need to finish the session
            clientRepository.delete(client);

            Client newClient = new Client();
            newClient.setGeneratedUniqueName(userName);
            clientRepository.save(newClient);
        } else {
            clientRepository.save(client);
        }

        simpMessagingTemplate.convertAndSendToUser(userName, "/channel", responseMsg);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = Objects.requireNonNull(headerAccessor.getUser()).getName();

        Client newClient = new Client();
        newClient.setGeneratedUniqueName(userName);
        clientRepository.save(newClient);

        chatBots.put(userName, new ChatBot());

//        System.out.println("Websocket connection established  " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = Objects.requireNonNull(headerAccessor.getUser()).getName();

        // For usual persistent db
        /*
        Client client = Objects.requireNonNull(clientRepository.findByGeneratedUniqueName(userName));
        if (client.getJobInfo() == null || client.getGeneratedUniqueName() == null) {
            clientRepository.delete(client);
        }
        */

        // Handle exception that omits because of using in-memory db
        try {
            Client client = Objects.requireNonNull(clientRepository.findByGeneratedUniqueName(userName));
            if (client.getJobInfo() == null || client.getGeneratedUniqueName() == null) {
                clientRepository.delete(client);
            }
        } catch (SQLGrammarException e) {
            System.err.println(e.getMessage());
        }

        chatBots.remove(userName);

        //        System.out.println("User disconnected : " + username);
    }
}
