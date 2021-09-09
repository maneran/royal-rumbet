package com.sheepit.app.service.criteria;

import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sheepit.app.domain.Game} entity. This class is used
 * in {@link com.sheepit.app.web.rest.GameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /games?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GameCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GameType
     */
    public static class GameTypeFilter extends Filter<GameType> {

        public GameTypeFilter() {}

        public GameTypeFilter(GameTypeFilter filter) {
            super(filter);
        }

        @Override
        public GameTypeFilter copy() {
            return new GameTypeFilter(this);
        }
    }

    /**
     * Class for filtering GameStageType
     */
    public static class GameStageTypeFilter extends Filter<GameStageType> {

        public GameStageTypeFilter() {}

        public GameStageTypeFilter(GameStageTypeFilter filter) {
            super(filter);
        }

        @Override
        public GameStageTypeFilter copy() {
            return new GameStageTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter dateStart;

    private GameTypeFilter gameType;

    private GameStageTypeFilter stageType;

    private LongFilter opponentAId;

    private LongFilter opponentBId;

    private LongFilter tournamentId;

    private StringFilter userId;

    public GameCriteria() {}

    public GameCriteria(GameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.gameType = other.gameType == null ? null : other.gameType.copy();
        this.stageType = other.stageType == null ? null : other.stageType.copy();
        this.opponentAId = other.opponentAId == null ? null : other.opponentAId.copy();
        this.opponentBId = other.opponentBId == null ? null : other.opponentBId.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public GameCriteria copy() {
        return new GameCriteria(this);
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

    public GameTypeFilter getGameType() {
        return gameType;
    }

    public GameTypeFilter gameType() {
        if (gameType == null) {
            gameType = new GameTypeFilter();
        }
        return gameType;
    }

    public void setGameType(GameTypeFilter gameType) {
        this.gameType = gameType;
    }

    public GameStageTypeFilter getStageType() {
        return stageType;
    }

    public GameStageTypeFilter stageType() {
        if (stageType == null) {
            stageType = new GameStageTypeFilter();
        }
        return stageType;
    }

    public void setStageType(GameStageTypeFilter stageType) {
        this.stageType = stageType;
    }

    public LongFilter getOpponentAId() {
        return opponentAId;
    }

    public LongFilter opponentAId() {
        if (opponentAId == null) {
            opponentAId = new LongFilter();
        }
        return opponentAId;
    }

    public void setOpponentAId(LongFilter opponentAId) {
        this.opponentAId = opponentAId;
    }

    public LongFilter getOpponentBId() {
        return opponentBId;
    }

    public LongFilter opponentBId() {
        if (opponentBId == null) {
            opponentBId = new LongFilter();
        }
        return opponentBId;
    }

    public void setOpponentBId(LongFilter opponentBId) {
        this.opponentBId = opponentBId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameCriteria that = (GameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(gameType, that.gameType) &&
            Objects.equals(stageType, that.stageType) &&
            Objects.equals(opponentAId, that.opponentAId) &&
            Objects.equals(opponentBId, that.opponentBId) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateStart, gameType, stageType, opponentAId, opponentBId, tournamentId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
            (gameType != null ? "type=" + gameType + ", " : "") +
            (stageType != null ? "stageType=" + stageType + ", " : "") +
            (opponentAId != null ? "opponentAId=" + opponentAId + ", " : "") +
            (opponentBId != null ? "opponentBId=" + opponentBId + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
