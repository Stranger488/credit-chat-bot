package com.example.creditchatbot.handler;

import com.example.creditchatbot.model.AnonymousUserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CreditChatBotHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();

        if (principal == null) {
            principal = new AnonymousUserPrincipal(UUID.randomUUID().toString());
        }

        return principal;
    }
}
