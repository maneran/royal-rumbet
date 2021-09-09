package com.sheepit.app.domain.enumeration;

/**
 * The OpponentType enumeration.
 */
public enum OpponentType {
    SINGLE("Single"),
    TEAM("Team");

    private final String value;

    OpponentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
