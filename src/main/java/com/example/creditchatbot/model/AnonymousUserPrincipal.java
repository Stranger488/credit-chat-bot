package com.example.creditchatbot.model;

import java.security.Principal;
import java.util.Objects;

public class AnonymousUserPrincipal implements Principal {
    private final String name;

    public AnonymousUserPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnonymousUserPrincipal that = (AnonymousUserPrincipal) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
