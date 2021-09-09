package com.sheepit.app.service.criteria;

import com.sheepit.app.domain.enumeration.OpponentType;
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
 * Criteria class for the {@link com.sheepit.app.domain.Opponent} entity. This class is used
 * in {@link com.sheepit.app.web.rest.OpponentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /opponents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OpponentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OpponentType
     */
    public static class OpponentTypeFilter extends Filter<OpponentType> {

        public OpponentTypeFilter() {}

        public OpponentTypeFilter(OpponentTypeFilter filter) {
            super(filter);
        }

        @Override
        public OpponentTypeFilter copy() {
            return new OpponentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private OpponentTypeFilter type;

    private IntegerFilter numberOfPlayers;

    public OpponentCriteria() {}

    public OpponentCriteria(OpponentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.numberOfPlayers = other.numberOfPlayers == null ? null : other.numberOfPlayers.copy();
    }

    @Override
    public OpponentCriteria copy() {
        return new OpponentCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public OpponentTypeFilter getType() {
        return type;
    }

    public OpponentTypeFilter type() {
        if (type == null) {
            type = new OpponentTypeFilter();
        }
        return type;
    }

    public void setType(OpponentTypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public IntegerFilter numberOfPlayers() {
        if (numberOfPlayers == null) {
            numberOfPlayers = new IntegerFilter();
        }
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(IntegerFilter numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OpponentCriteria that = (OpponentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(numberOfPlayers, that.numberOfPlayers)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, numberOfPlayers);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpponentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (numberOfPlayers != null ? "numberOfPlayers=" + numberOfPlayers + ", " : "") +
            "}";
    }
}
