package com.example.creditchatbot.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class AnonymousUserPrincipal implements Principal {

    private final String name;

}
