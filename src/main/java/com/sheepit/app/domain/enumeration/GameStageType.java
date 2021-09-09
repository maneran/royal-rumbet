package com.sheepit.app.domain.enumeration;

/**
 * The GameStageType enumeration.
 */
public enum GameStageType {
    ROUND_ROBIN("Round-Robin"),
    FIRST_FOUR("First Four"),
    ROUND_FOUR("Round 4"),
    FIRST_ROUND("1st round"),
    SECOND_ROUND("2nd round"),
    THIRD_ROUND("3rd round"),
    ROUND_FIVE("Round 5"),
    ROUND_SIX("Round 6"),
    ROUND_SEVEN("Round 7"),
    FOURTH_ROUND("4th round"),
    FIFTH_ROUND("5th round"),
    EIGHTH_FINALS("Eighth-finals"),
    QUARTER_FINALS("Quarterfinals"),
    SWEET_SIXTEEN("Sweet Sixteen"),
    ELITE_EIGHT("Elite Eight"),
    SEMI_FINALS("Semifinals"),
    FINAL_FOUR("Final Four"),
    FINAL("Final"),
    NATIONAL_CHAMPIONSHIP("National Championship");

    private final String value;

    GameStageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
