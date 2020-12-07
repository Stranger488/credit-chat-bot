package com.example.creditchatbot.util;

import com.example.creditchatbot.model.Client;
import com.example.creditchatbot.model.dto.ChatMessage;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import org.springframework.stereotype.Component;

import static com.example.creditchatbot.util.ChatBotCommonConstants.END_CMD;

@Component
public class ChatBot {

    private ChatBotState prevChatBotState;
    private ChatBotState currentChatBotState;

    public ChatBot() {
        this.prevChatBotState = ChatBotState.INIT;
        this.currentChatBotState = ChatBotState.JOIN;
    }

    public ChatBotState getCurrentState() {
        return currentChatBotState;
    }

    public void setCurrentState(ChatBotState currentChatBotState) {
        this.currentChatBotState = currentChatBotState;
    }

    public String getCurrentMessage() {
        return ChatBotMessagesDB.buildMessageFromState(currentChatBotState);
    }

    public String getCurrentDeclineMessage() {
        return ChatBotMessagesDB.buildDeclineMessageFromState(prevChatBotState);
    }

    public String getMsgByState(ChatBotState chatBotState) {
        return ChatBotMessagesDB.buildMessageFromState(chatBotState);
    }

    public boolean isBotCmdProceeded(String content, Client client) {
        if (END_CMD.equals(content)) {
            currentChatBotState = ChatBotState.END_SESSION;
            client.setGeneratedUniqueName(null); // For deleting client from repo

            return true;
        }

        return false;
    }

    public String processMsg(ChatMessage msg, Client client) {
        String content = msg.getContent().trim().toLowerCase();

        if (!isBotCmdProceeded(content, client)) { // Do this only if it is not bot command
            ChatBotState nextChatBotState = currentChatBotState.processState(content, client);
            prevChatBotState = currentChatBotState;
            setCurrentState(nextChatBotState);

            if (currentChatBotState == ChatBotState.DECLINE) {
                client.setGeneratedUniqueName(null); // For deleting client from repo

                String declineMsg = getMsgByState(currentChatBotState); // Get decline message

                this.nextState(); // Switch from DECLINE to END_SESSION

                return getCurrentDeclineMessage() + declineMsg + getCurrentMessage();
            } else if (currentChatBotState == ChatBotState.INIT) {
                String curMsg = getMsgByState(currentChatBotState); // Get init message

                this.nextState(); // Switch from INIT to CITY

                return curMsg + getCurrentMessage();
            } else if (currentChatBotState == ChatBotState.ERROR) {
                String curMsg = getCurrentMessage();

                setCurrentState(prevChatBotState); // Switch to state, where error was handled

                return curMsg + getCurrentMessage();
            }
        }

        return getCurrentMessage();
    }

    public void nextState() {
        ChatBotState nextChatBotState = currentChatBotState.nextState();
        setCurrentState(nextChatBotState);
    }
}
