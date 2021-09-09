package com.sheepit.app.domain.enumeration;

/**
 * The GameType enumeration.
 */
public enum GameType {
    SOCCER("Soccer"),
    BASKETBALL("Basketball"),
    TENNIS("Tennis");

    private final String value;

    GameType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
