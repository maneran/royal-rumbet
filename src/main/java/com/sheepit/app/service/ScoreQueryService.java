package com.sheepit.app.service;

import com.sheepit.app.domain.*; // for static metamodels
import com.sheepit.app.domain.Score;
import com.sheepit.app.repository.ScoreRepository;
import com.sheepit.app.service.criteria.ScoreCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Score} entities in the database.
 * The main input is a {@link ScoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Score} or a {@link Page} of {@link Score} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScoreQueryService extends QueryService<Score> {

    private final Logger log = LoggerFactory.getLogger(ScoreQueryService.class);

    private final ScoreRepository scoreRepository;

    public ScoreQueryService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * Return a {@link List} of {@link Score} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Score> findByCriteria(ScoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Score> specification = createSpecification(criteria);
        return scoreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Score} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Score> findByCriteria(ScoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Score> specification = createSpecification(criteria);
        return scoreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Score> specification = createSpecification(criteria);
        return scoreRepository.count(specification);
    }

    /**
     * Function to convert {@link ScoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Score> createSpecification(ScoreCriteria criteria) {
        Specification<Score> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Score_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Score_.points));
            }
            if (criteria.getGameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGameId(), root -> root.join(Score_.game, JoinType.LEFT).get(Game_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Score_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(Score_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
