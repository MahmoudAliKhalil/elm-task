package com.github.mahmoudalikhalil.elm.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN, DEALER, CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
