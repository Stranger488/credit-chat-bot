package com.example.creditchatbot.controller;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.service.CreditChatBotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class CreditChatBotController {

    private final CreditChatBotService creditChatBotService;

    public CreditChatBotController(CreditChatBotService creditChatBotService) {
        this.creditChatBotService = creditChatBotService;
    }

    @MessageMapping("/join")
    public ChatMessage joinUser(@Payload ChatMessage chatMessage, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            if (name != null) {
                creditChatBotService.addUserAndSendWelcome(name, chatMessage);
            }
        }

        return chatMessage;
    }

    @MessageMapping("/send")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            if (name != null) {
                creditChatBotService.sendMsgToUser(name, chatMessage);

                creditChatBotService.sendBotMsgToUser(name, chatMessage);
            }
        }

        return chatMessage;
    }

}
