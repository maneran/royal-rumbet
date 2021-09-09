package com.sheepit.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sheepit.app.domain.Tournament} entity. This class is used
 * in {@link com.sheepit.app.web.rest.TournamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter dateStart;

    private InstantFilter dateEnd;

    private InstantFilter registrationEndDate;

    private LongFilter gameId;

    public TournamentCriteria() {}

    public TournamentCriteria(TournamentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.dateEnd = other.dateEnd == null ? null : other.dateEnd.copy();
        this.registrationEndDate = other.registrationEndDate == null ? null : other.registrationEndDate.copy();
        this.gameId = other.gameId == null ? null : other.gameId.copy();
    }

    @Override
    public TournamentCriteria copy() {
        return new TournamentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public InstantFilter getDateStart() {
        return dateStart;
    }

    public InstantFilter dateStart() {
        if (dateStart == null) {
            dateStart = new InstantFilter();
        }
        return dateStart;
    }

    public void setDateStart(InstantFilter dateStart) {
        this.dateStart = dateStart;
    }

    public InstantFilter getDateEnd() {
        return dateEnd;
    }

    public InstantFilter dateEnd() {
        if (dateEnd == null) {
            dateEnd = new InstantFilter();
        }
        return dateEnd;
    }

    public void setDateEnd(InstantFilter dateEnd) {
        this.dateEnd = dateEnd;
    }

    public InstantFilter getRegistrationEndDate() {
        return registrationEndDate;
    }

    public InstantFilter registrationEndDate() {
        if (registrationEndDate == null) {
            registrationEndDate = new InstantFilter();
        }
        return registrationEndDate;
    }

    public void setRegistrationEndDate(InstantFilter registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentCriteria that = (TournamentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(dateEnd, that.dateEnd) &&
            Objects.equals(registrationEndDate, that.registrationEndDate) &&
            Objects.equals(gameId, that.gameId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateStart, dateEnd, registrationEndDate, gameId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
            (dateEnd != null ? "dateEnd=" + dateEnd + ", " : "") +
            (registrationEndDate != null ? "registrationEndDate=" + registrationEndDate + ", " : "") +
            (gameId != null ? "gameId=" + gameId + ", " : "") +
            "}";
    }
}
