package com.example.creditchatbot.util;

import com.example.creditchatbot.model.ChatMessage;
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

    public String getCurrentMessage(MessageBuilder messageBuilder) {
        return messageBuilder.buildMessageFromState(currentState);
    }

    public String getCurrentDeclineMessage(MessageBuilder messageBuilder) {
        return messageBuilder.buildDeclineMessageFromState(prevState);
    }

    public String getMsgByState(MessageBuilder messageBuilder, State state) {
        return messageBuilder.buildMessageFromState(state);
    }



    public String processMsg(ChatMessage msg, MessageBuilder msgBuilder) {
        String content = msg.getContent().trim().toLowerCase();

        State nextState = currentState.processState(content);
        prevState = currentState;
        setCurrentState(nextState);

        if (currentState == State.DECLINE) {
            String curMsg = getCurrentMessage(msgBuilder);
            return getCurrentDeclineMessage(msgBuilder)+ curMsg;
        } else if (currentState == State.INIT) {
            String curMsg = getMsgByState(msgBuilder, currentState); // Get init message

            this.nextState(); // Switch from INIT to IS_BANK_CLIENT

            return curMsg + getCurrentMessage(msgBuilder);
        } else if (currentState == State.ERROR) {
            String curMsg = getCurrentMessage(msgBuilder);

            setCurrentState(prevState); // Switch to state, where error was handled

            return curMsg + getCurrentMessage(msgBuilder);
        }

        return getCurrentMessage(msgBuilder);
    }

    public void nextState() {
        State nextState = currentState.nextState();
        setCurrentState(nextState);
    }
}
