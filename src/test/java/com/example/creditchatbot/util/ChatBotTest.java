package com.example.creditchatbot.util;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.model.Client;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import com.example.creditchatbot.util.staticDB.CitiesDB;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static com.example.creditchatbot.util.ChatBotCommonConstants.END_CMD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ChatBotTest {

    @Test
    void isBotCmdProceeded() {
        ChatBot chatBot = new ChatBot();
        Client client = new Client();
        String content = "test";
        client.setGeneratedUniqueName(content);

        assertFalse(chatBot.isBotCmdProceeded("command", client));
        assertEquals(content, client.getGeneratedUniqueName());
    }

    @Test
    void isBotCmdProceededEndCmd() {
        ChatBot chatBot = new ChatBot();
        Client client = new Client();
        client.setGeneratedUniqueName("test");

        assertTrue(chatBot.isBotCmdProceeded(END_CMD, client));
        assertNull(client.getGeneratedUniqueName());
    }

    /**
     * После соединения с сервером должен произойти переход из состояния
     * JOIN в INIT, затем переход в CITY и вывод первого сообщения
     */
    @Test
    void processJoinMsg() {
        processMsg(
                ChatMessage.MessageType.JOIN,
                "join",
                ChatBotState.JOIN,
                ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY)
        );
    }

    /**
     * Первым сообщением пользователь должен указать свой город,
     * после чего осуществляется переход из CITY в CHECK_CLIENT,
     * при этом происходит проверка по регулярному выражению и сравнение с
     * динамически обновляемым списком городов из внешнего сервиса.
     * Данный тест-кейс показывает коррректную работу.
     */
    @Test
    void processCityMsg() {
        try (MockedStatic<CitiesDB> citiesDBMockedStatic = mockStatic(CitiesDB.class)) {
            citiesDBMockedStatic
                    .when(() -> CitiesDB.isInDb("москва"))
                    .thenReturn(true);
        }

        processMsg(
                ChatMessage.MessageType.SEND,
                "Москва",
                ChatBotState.CITY,
                ChatBotMessagesDB.buildMessageFromState(ChatBotState.CHECK_CLIENT)
        );
    }

    /**
     * Данный тест-кейс показывает некорректный ввод,
     * перевод в состояние ошибки и возврат соответствующего сообщения.
     */
    @Test
    void processCityErrorMsg() {
        processMsg(
                ChatMessage.MessageType.SEND,
                "123Некорректный город",
                ChatBotState.CITY,
                ChatBotMessagesDB.buildMessageFromState(ChatBotState.ERROR)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY)
        );
    }

    /**
     * Данный тест-кейс показывает вариант работы метода, при котором
     * передан город, не обслуживаемый банком.
     */
    @Test
    void processCityDeclineMsg() {
        try (MockedStatic<CitiesDB> citiesDBMockedStatic = mockStatic(CitiesDB.class)) {
            citiesDBMockedStatic
                    .when(() -> CitiesDB.isInDb("липецк"))
                    .thenReturn(false);
        }

        Client client = processMsg(
                ChatMessage.MessageType.SEND,
                "Липецк",
                ChatBotState.CITY,
                ChatBotMessagesDB.buildDeclineMessageFromState(ChatBotState.CITY)
                        + ChatBotMessagesDB.buildMessageFromState(ChatBotState.DECLINE)
                        + ChatBotMessagesDB.buildMessageFromState(ChatBotState.END_SESSION)
        );
        assertNull(client.getGeneratedUniqueName());
    }

    /**
     * Общий метод для проверки processMsg для разных состояний
     */
    private Client processMsg(ChatMessage.MessageType type, String content,
                            ChatBotState state, String messageExpected) {
        ChatMessage chatMessage = ChatMessage.builder()
                .messageType(type)
                .sender("client")
                .content(content)
                .build();

        Client newClient = Client.builder()
                .generatedUniqueName(UUID.randomUUID().toString())
                .build();

        ChatBot chatBot = new ChatBot();
        chatBot.setCurrentState(state);

        assertEquals(
                messageExpected,
                chatBot.processMsg(chatMessage, newClient)
        );

        return newClient;
    }
}