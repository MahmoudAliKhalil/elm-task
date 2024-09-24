package com.github.mahmoudalikhalil.elm.model.entity;

public enum Status {
    ACTIVE, INACTIVE;

    public Status flipStatus() {
        if (this == ACTIVE) {
            return INACTIVE;
        }
        return ACTIVE;
    }
}
