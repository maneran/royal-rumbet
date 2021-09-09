package com.sheepit.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sheepit.app.domain.Score} entity. This class is used
 * in {@link com.sheepit.app.web.rest.ScoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter points;

    private LongFilter gameId;

    private StringFilter userId;

    private LongFilter tournamentId;

    public ScoreCriteria() {}

    public ScoreCriteria(ScoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.gameId = other.gameId == null ? null : other.gameId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
    }

    @Override
    public ScoreCriteria copy() {
        return new ScoreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public IntegerFilter points() {
        if (points == null) {
            points = new IntegerFilter();
        }
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public LongFilter getGameId() {
        return gameId;
    }

    public LongFilter gameId() {
        if (gameId == null) {
            gameId = new LongFilter();
        }
        return gameId;
    }

    public void setGameId(LongFilter gameId) {
        this.gameId = gameId;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public LongFilter tournamentId() {
        if (tournamentId == null) {
            tournamentId = new LongFilter();
        }
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ScoreCriteria that = (ScoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(gameId, that.gameId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, gameId, userId, tournamentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScoreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (gameId != null ? "gameId=" + gameId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            "}";
    }
}
