package ru.dsoccer1980.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, RESTAURANT;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
