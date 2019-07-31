package ru.dsoccer1980.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, COMPANY;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
