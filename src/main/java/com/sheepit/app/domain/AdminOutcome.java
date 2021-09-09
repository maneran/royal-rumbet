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
 * AdminOutcome entity.\n@author ranma.
 */
@ApiModel(description = "AdminOutcome entity.\n@author ranma.")
@Entity
@Table(name = "admin_outcome")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@IdClass(AdminOutcomeId.class)
public class AdminOutcome implements Serializable {

    private static final long serialVersionUID = 1L;

    //    @Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    //    @SequenceGenerator(name = "sequenceGenerator")
    @Column(insertable = false, updatable = false, columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    private Long id;

    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "end_outcome_opponent_a")
    private String endOutcomeOpponentA;

    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "end_outcome_opponent_b")
    private String endOutcomeOpponentB;

    @Id
    @Column(name = "game_id")
    @JsonIgnoreProperties(value = { "opponentA", "opponentB", "tournament", "users" }, allowSetters = true)
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

    //    @JsonIgnoreProperties(value = { "opponentA", "opponentB", "tournament", "users" }, allowSetters = true)
    //    @OneToOne(optional = false)
    //    @NotNull
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

    public AdminOutcome id(Long id) {
        this.id = id;
        return this;
    }

    public String getEndOutcomeOpponentA() {
        return this.endOutcomeOpponentA;
    }

    public AdminOutcome endOutcomeOpponentA(String endOutcomeOpponentA) {
        this.endOutcomeOpponentA = endOutcomeOpponentA;
        return this;
    }

    public void setEndOutcomeOpponentA(String endOutcomeOpponentA) {
        this.endOutcomeOpponentA = endOutcomeOpponentA;
    }

    public String getEndOutcomeOpponentB() {
        return this.endOutcomeOpponentB;
    }

    public AdminOutcome endOutcomeOpponentB(String endOutcomeOpponentB) {
        this.endOutcomeOpponentB = endOutcomeOpponentB;
        return this;
    }

    public void setEndOutcomeOpponentB(String endOutcomeOpponentB) {
        this.endOutcomeOpponentB = endOutcomeOpponentB;
    }

    public Game getGame() {
        return this.game;
    }

    public AdminOutcome game(Game game) {
        this.setGame(game);
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return this.user;
    }

    public AdminOutcome user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public AdminOutcome tournament(Tournament tournament) {
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
        if (!(o instanceof AdminOutcome)) {
            return false;
        }
        return id != null && id.equals(((AdminOutcome) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminOutcome{" +
            "id=" + getId() +
            ", endOutcomeOpponentA='" + getEndOutcomeOpponentA() + "'" +
            ", endOutcomeOpponentB='" + getEndOutcomeOpponentB() + "'" +
            "}";
    }
}
