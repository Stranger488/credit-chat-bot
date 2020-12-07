package com.example.creditchatbot.util;

import com.example.creditchatbot.model.Client;

public interface IChatBotState {
    default ChatBotState nextState() {
        return ChatBotState.INIT;
    }

    default ChatBotState nextDeclineState() {
        return ChatBotState.DECLINE;
    }

    default ChatBotState nextOnErrorState() {
        return ChatBotState.ERROR;
    }

    default ChatBotState processState(String content, Client client) {
        return ChatBotState.INIT;
    }
}