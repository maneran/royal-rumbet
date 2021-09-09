package com.sheepit.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Game entity.\n@author ranma.\nname will be regenerate including\ntournament-name + opponent names + dateStart\nfor searching purpose in Outcome endpoint
 */
@ApiModel(
    description = "Game entity.\n@author ranma.\nname will be regenerate including\ntournament-name + opponent names + dateStart\nfor searching purpose in Outcome endpoint"
)
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private Instant dateStart;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false)
    private GameStageType stageType;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Opponent opponentA;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Opponent opponentB;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "games" }, allowSetters = true)
    private Tournament tournament;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_game__user", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateStart() {
        return this.dateStart;
    }

    public Game dateStart(Instant dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public Game gameType(GameType gameType) {
        this.gameType = gameType;
        return this;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameStageType getStageType() {
        return this.stageType;
    }

    public Game stageType(GameStageType stageType) {
        this.stageType = stageType;
        return this;
    }

    public void setStageType(GameStageType stageType) {
        this.stageType = stageType;
    }

    public Opponent getOpponentA() {
        return this.opponentA;
    }

    public Game opponentA(Opponent opponent) {
        this.setOpponentA(opponent);
        return this;
    }

    public void setOpponentA(Opponent opponent) {
        this.opponentA = opponent;
    }

    public Opponent getOpponentB() {
        return this.opponentB;
    }

    public Game opponentB(Opponent opponent) {
        this.setOpponentB(opponent);
        return this;
    }

    public void setOpponentB(Opponent opponent) {
        this.opponentB = opponent;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public Game tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Game users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Game addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Game removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", type='" + getGameType() + "'" +
            ", stageType='" + getStageType() + "'" +
            "}";
    }
}
