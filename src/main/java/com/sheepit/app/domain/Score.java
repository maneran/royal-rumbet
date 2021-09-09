package com.sheepit.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 * Score entity.\n@author ranma.
 */
@ApiModel(description = "Score entity.\n@author ranma.")
@Entity
@Table(name = "score")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@IdClass(ScoreId.class)
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    //    @Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    //    @SequenceGenerator(name = "sequenceGenerator")
    @Column(insertable = false, updatable = false, columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    private Long id;

    @Column(name = "points")
    private Integer points;

    @Id
    @Column(name = "game_id")
    @JsonIgnoreProperties(value = { "tournament", "users" }, allowSetters = true)
    //    @OneToOne(optional = false)
    @NotNull
    private Long gameId;

    @Id
    @Column(name = "user_id")
    //    @OneToOne(optional = false)
    @NotNull
    private String userId;

    @Id
    @Column(name = "tournament_id")
    @JsonIgnoreProperties(value = { "games" }, allowSetters = true)
    //    @OneToOne(optional = false)
    @NotNull
    private Long tournamentId;

    //    @JoinColumn(unique = true)
    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    //    @OneToOne(optional = false)
    //    @NotNull
    //    @JoinColumn(unique = true)
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    //    @JsonIgnoreProperties(value = { "games" }, allowSetters = true)
    //    @OneToOne(optional = false)
    //    @NotNull
    //    @JoinColumn(unique = true)
    @ManyToOne
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Score id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Score points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Game getGame() {
        return this.game;
    }

    public Score game(Game game) {
        this.setGame(game);
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return this.user;
    }

    public Score user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public Score tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Score)) {
            return false;
        }
        return id != null && id.equals(((Score) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
