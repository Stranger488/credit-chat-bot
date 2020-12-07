package com.example.creditchatbot.util;

import com.example.creditchatbot.model.dto.ChatMessage;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import org.springframework.stereotype.Component;

@Component
public class ChatBot {

    private State prevState;
    private State currentState;

    public ChatBot() {
        this.prevState = State.INIT;
        this.currentState = State.JOIN;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getCurrentMessage() {
        return ChatBotMessagesDB.buildMessageFromState(currentState);
    }

    public String getCurrentDeclineMessage() {
        return ChatBotMessagesDB.buildDeclineMessageFromState(prevState);
    }

    public String getMsgByState(State state) {
        return ChatBotMessagesDB.buildMessageFromState(state);
    }

    public String processMsg(ChatMessage msg) {
        String content = msg.getContent().trim().toLowerCase();

        State nextState = currentState.processState(content);
        prevState = currentState;
        setCurrentState(nextState);

        if (currentState == State.DECLINE) {
            String curMsg = getCurrentMessage();
            return getCurrentDeclineMessage() + curMsg;
        } else if (currentState == State.INIT) {
            String curMsg = getMsgByState(currentState); // Get init message

            this.nextState(); // Switch from INIT to IS_BANK_CLIENT

            return curMsg + getCurrentMessage();
        } else if (currentState == State.ERROR) {
            String curMsg = getCurrentMessage();

            setCurrentState(prevState); // Switch to state, where error was handled

            return curMsg + getCurrentMessage();
        }

        return getCurrentMessage();
    }

    public void nextState() {
        State nextState = currentState.nextState();
        setCurrentState(nextState);
    }
}
