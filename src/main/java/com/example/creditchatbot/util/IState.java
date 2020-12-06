package com.example.creditchatbot.util;

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

    default State processState(String content) {
        return State.INIT;
    }
}