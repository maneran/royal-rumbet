package com.sheepit.app.domain;

import com.sheepit.app.domain.enumeration.GameOutcome;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * GameConfig entity.\n@author ranma.
 */
@ApiModel(description = "GameConfig entity.\n@author ranma.")
@Entity
@Table(name = "game_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GameConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "game_stage_type", nullable = false)
    private GameStageType gameStageType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "game_outcome", nullable = false)
    private GameOutcome gameOutcome;

    @NotNull
    @Min(value = 0)
    @Max(value = 20)
    @Column(name = "points", nullable = false)
    private Integer points;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameConfig id(Long id) {
        this.id = id;
        return this;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public GameConfig gameType(GameType gameType) {
        this.gameType = gameType;
        return this;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameStageType getGameStageType() {
        return this.gameStageType;
    }

    public GameConfig gameStageType(GameStageType gameStageType) {
        this.gameStageType = gameStageType;
        return this;
    }

    public void setGameStageType(GameStageType gameStageType) {
        this.gameStageType = gameStageType;
    }

    public GameOutcome getGameOutcome() {
        return this.gameOutcome;
    }

    public GameConfig gameOutcome(GameOutcome gameOutcome) {
        this.gameOutcome = gameOutcome;
        return this;
    }

    public void setGameOutcome(GameOutcome gameOutcome) {
        this.gameOutcome = gameOutcome;
    }

    public Integer getPoints() {
        return this.points;
    }

    public GameConfig points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameConfig)) {
            return false;
        }
        return id != null && id.equals(((GameConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameConfig{" +
            "id=" + getId() +
            ", gameType='" + getGameType() + "'" +
            ", gameStageType='" + getGameStageType() + "'" +
            ", gameOutcome='" + getGameOutcome() + "'" +
            ", points=" + getPoints() +
            "}";
    }
}
