package com.example.creditchatbot.util;

import com.example.creditchatbot.model.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatBot {

    public enum State {
        INIT,
        IS_BANK_CLIENT,
        IS_CITIZEN,
        CITY,
        AMOUNT_TERM,
        RATE,
        AGE,
        JOB,
        INCOME
    }

    private State currentState;

    public ChatBot() {
        this.currentState = State.IS_BANK_CLIENT;
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

    public void processMsg(ChatMessage msg) {
        String content = msg.getContent();

        if (currentState == State.INIT) {
            this.setCurrentState(State.IS_BANK_CLIENT);
        } else if (currentState == State.IS_BANK_CLIENT) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.IS_CITIZEN);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.IS_CITIZEN);
            }
        } else if (currentState == State.IS_CITIZEN) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.CITY);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.CITY);
            }
        } else if (currentState == State.CITY) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.AMOUNT_TERM);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.AMOUNT_TERM);
            }
        } else if (currentState == State.AMOUNT_TERM) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.RATE);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.RATE);
            }
        } else if (currentState == State.RATE) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.AGE);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.AGE);
            }
        } else if (currentState == State.AGE) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.JOB);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.JOB);
            }
        } else if (currentState == State.JOB) {
            if ("Да".equals(content)) {
                this.setCurrentState(State.INCOME);
            } else if ("Нет".equals(content)) {
                this.setCurrentState(State.INCOME);
            }
        }
    }
}
