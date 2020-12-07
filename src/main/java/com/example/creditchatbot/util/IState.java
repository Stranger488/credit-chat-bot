package com.example.creditchatbot.util;

import com.example.creditchatbot.model.Client;

public interface IState {
    default State nextState() {
        return State.INIT;
    }

    default State nextDeclineState() {
        return State.DECLINE;
    }

    default State nextOnErrorState() {
        return State.ERROR;
    }

    default State processState(String content, Client client) {
        return State.INIT;
    }
}