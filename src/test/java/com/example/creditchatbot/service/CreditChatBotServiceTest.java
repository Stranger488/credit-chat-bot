package com.example.creditchatbot.service;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.model.Client;
import com.example.creditchatbot.repository.ClientRepository;
import com.example.creditchatbot.util.ChatBot;
import com.example.creditchatbot.util.ChatBotState;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
class CreditChatBotServiceTest {

    @SpyBean
    private CreditChatBotService creditChatBotService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private ClientRepository clientRepository;

    /**
     * Метод проверяет факт вызова методов
     * для отправки клиенту приветственных сообщений
     */
    @Test
    void addUserAndSendWelcome() {
        String uniqueName = "uniqueName";

        HashMap<String, ChatBot> chatBots =
                (HashMap<String, ChatBot>) ReflectionTestUtils.getField(creditChatBotService, "chatBots");
        chatBots.put(uniqueName, new ChatBot());

        creditChatBotService.addUserAndSendWelcome(uniqueName);

        ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ChatMessage> chatMessageCapture = ArgumentCaptor.forClass(ChatMessage.class);

        Mockito.verify(simpMessagingTemplate, times(2))
                .convertAndSendToUser(
                        nameCapture.capture(),
                        destinationCapture.capture(),
                        chatMessageCapture.capture()
                );

        List<String> nameAllValues = nameCapture.getAllValues();
        List<String> destinationAllValues = destinationCapture.getAllValues();
        List<ChatMessage> chatMessageAllValues = chatMessageCapture.getAllValues();

        assertEquals(List.of(uniqueName, uniqueName), nameAllValues);
        assertEquals(List.of("/channel", "/channel"), destinationAllValues);
        assertEquals(
                List.of(
                        ChatMessage.builder()
                                .sender("server")
                                .messageType(ChatMessage.MessageType.JOIN)
                                .content(ChatBotMessagesDB.buildMessageFromState(ChatBotState.JOIN))
                                .build(),
                        ChatMessage.builder()
                                .sender("server")
                                .messageType(ChatMessage.MessageType.SEND)
                                .content(
                                        ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                                                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY)
                                )
                                .build()
                ),
                chatMessageAllValues
        );
    }

    /**
     * Метод должен изменить messageType и отправить сообщение клиенту
     */
    @Test
    void sendUserMsgToChannel() {
        String uniqueName = "uniqueName";

        creditChatBotService.sendUserMsgToChannel(
                uniqueName,
                ChatMessage.builder().build()
        );

        Mockito.verify(simpMessagingTemplate)
                .convertAndSendToUser(
                        uniqueName,
                        "/channel",
                        ChatMessage.builder()
                                .messageType(ChatMessage.MessageType.SEND)
                                .build()
                );
    }

    /**
     * Данные корректны, бот отсылает ответ клиенту
     */
    @Test
    void sendBotMsgToUser() {
        String uniqueName = "uniqueName";

        HashMap<String, ChatBot> chatBots =
                (HashMap<String, ChatBot>) ReflectionTestUtils.getField(creditChatBotService, "chatBots");
        ChatBot chatBot = new ChatBot();
        chatBot.setCurrentState(ChatBotState.CITY);
        chatBots.put(uniqueName, chatBot);

        Client client = Client.builder()
                .generatedUniqueName(uniqueName)
                .build();
        Mockito.doReturn(client)
                .when(clientRepository)
                .findByGeneratedUniqueName(uniqueName);

        creditChatBotService.sendBotMsgToUser(
                uniqueName,
                ChatMessage.builder()
                        .messageType(ChatMessage.MessageType.SEND)
                        .content("москва")
                        .build()
        );

        Mockito.verify(clientRepository)
                .save(client);

        Mockito.verify(simpMessagingTemplate)
                .convertAndSendToUser(
                        uniqueName,
                        "/channel",
                        ChatMessage.builder()
                                .sender("server")
                                .content(ChatBotMessagesDB.buildMessageFromState(ChatBotState.CHECK_CLIENT))
                                .messageType(ChatMessage.MessageType.SEND)
                                .build()
                );
    }

    /**
     * Данные не подходят для подачи заявки,
     * бот отсылает соответствующий ответ и завершает сеанс, удаляя клиента.
     */
    @Test
    void sendBotMsgToUserEndSession() {
        String uniqueName = "uniqueName";

        HashMap<String, ChatBot> chatBots =
                (HashMap<String, ChatBot>) ReflectionTestUtils.getField(creditChatBotService, "chatBots");
        ChatBot chatBot = new ChatBot();
        chatBot.setCurrentState(ChatBotState.CITY);
        chatBots.put(uniqueName, chatBot);

        Client client = Client.builder()
                .generatedUniqueName(uniqueName)
                .build();
        Mockito.doReturn(client)
                .when(clientRepository)
                .findByGeneratedUniqueName(uniqueName);

        creditChatBotService.sendBotMsgToUser(
                uniqueName,
                ChatMessage.builder()
                        .messageType(ChatMessage.MessageType.SEND)
                        .content("Липецк")
                        .build()
        );

        Mockito.verify(clientRepository)
                .delete(client);
        Mockito.verify(clientRepository)
                .save(Client.builder()
                        .generatedUniqueName(uniqueName)
                        .build());

        Mockito.verify(simpMessagingTemplate)
                .convertAndSendToUser(
                        uniqueName,
                        "/channel",
                        ChatMessage.builder()
                                .sender("server")
                                .messageType(ChatMessage.MessageType.SEND)
                                .content(
                                        ChatBotMessagesDB.buildDeclineMessageFromState(ChatBotState.CITY)
                                                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.DECLINE)
                                                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.END_SESSION)
                                )
                                .build()
                );
    }

    /**
     * Обработка подключения нового пользователя
     */
    @Test
    void handleWebSocketConnectListener() {
        String uniqueName = "uniqueName";
        SessionConnectEvent sessionConnectEventMock = mock(SessionConnectEvent.class);

        doReturn(uniqueName)
                .when(creditChatBotService)
                .getUserNameFromEvent(sessionConnectEventMock);

        creditChatBotService.handleWebSocketConnectListener(sessionConnectEventMock);

        Mockito.verify(clientRepository)
                .save(
                        Client.builder()
                                .generatedUniqueName(uniqueName)
                                .build()
                );

        HashMap<String, ChatBot> chatBots =
                (HashMap<String, ChatBot>) ReflectionTestUtils.getField(creditChatBotService, "chatBots");
        assertFalse(chatBots.isEmpty());
    }

    /**
     * Обработка отключения пользователя
     */
    @Test
    void handleWebSocketDisconnectListener() {
        String uniqueName = "uniqueName";
        SessionDisconnectEvent sessionDisconnectEventMock = mock(SessionDisconnectEvent.class);

        doReturn(uniqueName)
                .when(creditChatBotService)
                .getUserNameFromEvent(sessionDisconnectEventMock);

        Client client = Client.builder()
                .build();
        Mockito.doReturn(client)
                .when(clientRepository)
                .findByGeneratedUniqueName(uniqueName);

        creditChatBotService.handleWebSocketDisconnectListener(sessionDisconnectEventMock);

        Mockito.verify(clientRepository)
                .delete(client);

        HashMap<String, ChatBot> chatBots =
                (HashMap<String, ChatBot>) ReflectionTestUtils.getField(creditChatBotService, "chatBots");
        assertFalse(chatBots.containsKey(uniqueName));
    }

}