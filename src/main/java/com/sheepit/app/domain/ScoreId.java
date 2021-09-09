package com.sheepit.app.domain;

import java.io.Serializable;
import java.util.Objects;

public class ScoreId implements Serializable {

    private Long gameId;

    private String userId;

    private Long tournamentId;

    public ScoreId() {}

    public ScoreId(Long gameId, String userId, Long tournamentId) {
        this.gameId = gameId;
        this.userId = userId;
        this.tournamentId = tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreId that = (ScoreId) o;
        return (
            Objects.equals(gameId, that.gameId) && Objects.equals(userId, that.userId) && Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, userId, tournamentId);
    }
}
