package com.sheepit.app.domain.enumeration;

/**
 * The GameOutcome enumeration.
 */
public enum GameOutcome {
    MISSED("Missed"),
    HIT("Hit"),
    SPECIAL("Special");

    private final String value;

    GameOutcome(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
