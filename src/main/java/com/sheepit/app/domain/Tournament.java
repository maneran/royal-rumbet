package com.sheepit.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Tournament entity.\n@author ranma.
 */
@ApiModel(description = "Tournament entity.\n@author ranma.")
@Entity
@Table(name = "tournament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private Instant dateStart;

    @NotNull
    @Column(name = "date_end", nullable = false)
    private Instant dateEnd;

    @NotNull
    @Column(name = "registration_end_date", nullable = false)
    private Instant registrationEndDate;

    @OneToMany(mappedBy = "tournament")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opponentA", "opponentB", "tournament", "users" }, allowSetters = true)
    private Set<Game> games = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tournament name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateStart() {
        return this.dateStart;
    }

    public Tournament dateStart(Instant dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getDateEnd() {
        return this.dateEnd;
    }

    public Tournament dateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Instant getRegistrationEndDate() {
        return this.registrationEndDate;
    }

    public Tournament registrationEndDate(Instant registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
        return this;
    }

    public void setRegistrationEndDate(Instant registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public Tournament games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public Tournament addGame(Game game) {
        this.games.add(game);
        game.setTournament(this);
        return this;
    }

    public Tournament removeGame(Game game) {
        this.games.remove(game);
        game.setTournament(null);
        return this;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.setTournament(null));
        }
        if (games != null) {
            games.forEach(i -> i.setTournament(this));
        }
        this.games = games;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tournament)) {
            return false;
        }
        return id != null && id.equals(((Tournament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", registrationEndDate='" + getRegistrationEndDate() + "'" +
            "}";
    }
}
