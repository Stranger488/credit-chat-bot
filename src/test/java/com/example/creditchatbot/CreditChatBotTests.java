package com.example.creditchatbot;

import com.example.creditchatbot.model.ChatMessage;
import com.example.creditchatbot.util.ChatBotState;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreditChatBotTests {

    @LocalServerPort
    private Integer port;

    private static final String JOIN_ENDPOINT = "/credit-chat-bot/join";

    private static final String SEND_ENDPOINT = "/credit-chat-bot/send";

    private static final String SUBSCRIBE_ENDPOINT = "/user/channel";

    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
        );
    }

    @Test
    void verifyJoin() throws Exception {
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        StompSession session = makeStompSession(blockingQueue);
        assertJoin(blockingQueue, session);
    }

    @Test
    void verifySend() throws Exception {
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        StompSession session = makeStompSession(blockingQueue);

        // Клиент посылает join чат-боту, последний отвечает приветствием
        assertJoin(blockingQueue, session);

        // Далее чат-бот посылает сообщение о начале нового сеанса и первый вопрос
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY));
    }

    @Test
    void verifyAllPositive() throws Exception {
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        StompSession session = makeStompSession(blockingQueue);

        // Клиент посылает join чат-боту, последний отвечает приветствием
        assertJoin(blockingQueue, session);

        // Далее чат-бот посылает сообщение о начале нового сеанса и первый вопрос
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY));
        // Клиент отвечает корректным городом
        assertSendClientChatMessage(session, blockingQueue, "Москва");

        // Чат-бот посылает базовый вопрос
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.CHECK_CLIENT));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Да");

        // Чат-бот посылает вопрос о сумме кредита
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.AMOUNT));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "20000");

        // Чат-бот посылает вопрос о сроке кредита
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.TERM));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "12");

        // Чат-бот посылает вопрос о ставке по кредиту
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.RATE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "12.4");

        // Чат-бот посылает вопрос о пользовательском соглашении
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.USER_AGREE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Да");

        // Чат-бот посылает вопрос о фамилии клиента
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.LAST_NAME));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Иванов");

        // Чат-бот посылает вопрос об имени клиента
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.FIRST_NAME));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Иван");

        // Чат-бот посылает вопрос об отчестве клиента
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.MIDDLE_NAME));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Иванович");

        // Чат-бот посылает вопрос о дате выдачи паспорта
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.PASSPORT_DATE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "01.01.1970");

        // Чат-бот посылает вопрос о номере паспорта
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.PASSPORT_NUM));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "0000111111");

        // Чат-бот посылает вопрос об организации, выдавшей паспорт
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.PASSPORT_ORG));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "ТП УФМС ПО ГОР. ГОРОДУ");

        // Чат-бот посылает вопрос о дате рождения
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.BIRTH_DATE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "01.01.1970");

        // Чат-бот посылает вопрос о номере телефона
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.PHONE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "70000000000");

        // Чат-бот посылает вопрос об адресе
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.ADDRESS));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "РФ, гор. Город, ул. Уличная, д. домный");

        // Чат-бот посылает вопрос о месте работы
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.JOB_PLACE));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Рабочая работа");

        // Чат-бот посылает вопрос о должности на работе
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.JOB_POSITION));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "Должностной должностник");

        // Чат-бот посылает вопрос об опыте работы
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.JOB_EXP));
        // Клиент отвечает корректно
        assertSendClientChatMessage(session, blockingQueue, "20");

        // Чат-бот посылает сообщение об успешно оформленной заявке
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.SUCCESS));
    }

    @Test
    void verify_incorrectInputCityAndThenValidCity() throws Exception {
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        StompSession session = makeStompSession(blockingQueue);

        // Клиент посылает join чат-боту, последний отвечает приветствием
        assertJoin(blockingQueue, session);

        // Далее чат-бот посылает сообщение о начале нового сеанса и первый вопрос
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY));
        // Клиент отвечает некорректными данными
        assertSendClientChatMessage(session, blockingQueue, "<<!>>городище<<?>>");

        // Далее чат-бот посылает сообщение о некорректных данных и предлагает снова ввести город
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.ERROR)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY));

        // Клиент отвечает корректными данными
        assertSendClientChatMessage(session, blockingQueue, "Москва");

        // Чат-бот посылает базовый вопрос, город корректно обработан
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.CHECK_CLIENT));
    }

    @Test
    void verify_absentCity() throws Exception {
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        StompSession session = makeStompSession(blockingQueue);

        // Клиент посылает join чат-боту, последний отвечает приветствием
        assertJoin(blockingQueue, session);

        // Далее чат-бот посылает сообщение о начале нового сеанса и первый вопрос
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildMessageFromState(ChatBotState.INIT)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.CITY));
        // Клиент отвечает некорректными данными
        assertSendClientChatMessage(session, blockingQueue, "Липецк");

        // Далее чат-бот посылает сообщение о некорректных данных и завершает сеанс, так как город не обслуживается
        assertSendServerChatMessage(blockingQueue, ChatBotMessagesDB.buildDeclineMessageFromState(ChatBotState.CITY)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.DECLINE)
                + ChatBotMessagesDB.buildMessageFromState(ChatBotState.END_SESSION));
    }

    private void assertJoin(BlockingQueue<ChatMessage> blockingQueue, StompSession session) throws InterruptedException {
        // Клиент посылает join, затем чат-бот отвечает, выслушиваем из канала ответ чат-бота
        ChatMessage joinMessage = ChatMessage.builder()
                .messageType(ChatMessage.MessageType.JOIN)
                .sender("client")
                .content("join")
                .build();
        ChatMessage joinMessageFromServer = ChatMessage.builder()
                .messageType(ChatMessage.MessageType.JOIN)
                .sender("server")
                .content(ChatBotMessagesDB.buildMessageFromState(ChatBotState.JOIN))
                .build();
        session.send(JOIN_ENDPOINT, joinMessage);
        await().atLeast(Duration.ofSeconds(3));
        assertEquals(joinMessageFromServer, blockingQueue.take());
    }

    private void assertSendClientChatMessage(StompSession session, BlockingQueue<ChatMessage> blockingQueue, String content) throws InterruptedException {
        ChatMessage sendMessage = ChatMessage.builder()
                .messageType(ChatMessage.MessageType.SEND)
                .sender("client")
                .content(content)
                .build();
        session.send(SEND_ENDPOINT, sendMessage);
        await().atLeast(Duration.ofSeconds(3));
        assertEquals(sendMessage, blockingQueue.take());
    }

    private void assertSendServerChatMessage(BlockingQueue<ChatMessage> blockingQueue, String content) throws InterruptedException {
        ChatMessage sendMessageFromServer = ChatMessage.builder()
                .messageType(ChatMessage.MessageType.SEND)
                .sender("server")
                .content(content)
                .build();
        await().atLeast(Duration.ofSeconds(3));
        assertEquals(sendMessageFromServer, blockingQueue.take());
    }

    private StompSession makeStompSession(BlockingQueue<ChatMessage> blockingQueue) throws InterruptedException, ExecutionException, TimeoutException {
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe(SUBSCRIBE_ENDPOINT, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((ChatMessage) payload);
            }
        });

        return session;
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/ws", port);
    }

}
