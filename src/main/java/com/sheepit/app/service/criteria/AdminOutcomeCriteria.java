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
 * Criteria class for the {@link com.sheepit.app.domain.AdminOutcome} entity. This class is used
 * in {@link com.sheepit.app.web.rest.AdminOutcomeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /admin-outcomes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdminOutcomeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter endOutcomeOpponentA;

    private StringFilter endOutcomeOpponentB;

    private LongFilter gameId;

    private StringFilter userId;

    private LongFilter tournamentId;

    public AdminOutcomeCriteria() {}

    public AdminOutcomeCriteria(AdminOutcomeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.endOutcomeOpponentA = other.endOutcomeOpponentA == null ? null : other.endOutcomeOpponentA.copy();
        this.endOutcomeOpponentB = other.endOutcomeOpponentB == null ? null : other.endOutcomeOpponentB.copy();
        this.gameId = other.gameId == null ? null : other.gameId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
    }

    @Override
    public AdminOutcomeCriteria copy() {
        return new AdminOutcomeCriteria(this);
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

    public StringFilter getEndOutcomeOpponentA() {
        return endOutcomeOpponentA;
    }

    public StringFilter endOutcomeOpponentA() {
        if (endOutcomeOpponentA == null) {
            endOutcomeOpponentA = new StringFilter();
        }
        return endOutcomeOpponentA;
    }

    public void setEndOutcomeOpponentA(StringFilter endOutcomeOpponentA) {
        this.endOutcomeOpponentA = endOutcomeOpponentA;
    }

    public StringFilter getEndOutcomeOpponentB() {
        return endOutcomeOpponentB;
    }

    public StringFilter endOutcomeOpponentB() {
        if (endOutcomeOpponentB == null) {
            endOutcomeOpponentB = new StringFilter();
        }
        return endOutcomeOpponentB;
    }

    public void setEndOutcomeOpponentB(StringFilter endOutcomeOpponentB) {
        this.endOutcomeOpponentB = endOutcomeOpponentB;
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
        final AdminOutcomeCriteria that = (AdminOutcomeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(endOutcomeOpponentA, that.endOutcomeOpponentA) &&
            Objects.equals(endOutcomeOpponentB, that.endOutcomeOpponentB) &&
            Objects.equals(gameId, that.gameId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, endOutcomeOpponentA, endOutcomeOpponentB, gameId, userId, tournamentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminOutcomeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (endOutcomeOpponentA != null ? "endOutcomeOpponentA=" + endOutcomeOpponentA + ", " : "") +
            (endOutcomeOpponentB != null ? "endOutcomeOpponentB=" + endOutcomeOpponentB + ", " : "") +
            (gameId != null ? "gameId=" + gameId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            "}";
    }
}
