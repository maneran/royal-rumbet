package com.sheepit.app.domain;

import com.sheepit.app.domain.enumeration.OpponentType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Opponent entity.\n@author ranma.
 */
@ApiModel(description = "Opponent entity.\n@author ranma.")
@Entity
@Table(name = "opponent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Opponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "opponentType", nullable = false)
    private OpponentType opponentType;

    @Min(value = 1)
    @Max(value = 11)
    @Column(name = "number_of_players")
    private Integer numberOfPlayers;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Opponent id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Opponent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Opponent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OpponentType getOpponentType() {
        return this.opponentType;
    }

    public Opponent type(OpponentType opponentType) {
        this.opponentType = opponentType;
        return this;
    }

    public void setType(OpponentType type) {
        this.opponentType = opponentType;
    }

    public Integer getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public Opponent numberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        return this;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opponent)) {
            return false;
        }
        return id != null && id.equals(((Opponent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Opponent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", opponentType='" + getOpponentType() + "'" +
            ", numberOfPlayers=" + getNumberOfPlayers() +
            "}";
    }
}
