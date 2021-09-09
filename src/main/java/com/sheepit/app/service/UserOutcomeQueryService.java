package com.sheepit.app.service;

import com.sheepit.app.domain.*; // for static metamodels
import com.sheepit.app.domain.UserOutcome;
import com.sheepit.app.repository.UserOutcomeRepository;
import com.sheepit.app.service.criteria.UserOutcomeCriteria;
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
 * Service for executing complex queries for {@link UserOutcome} entities in the database.
 * The main input is a {@link UserOutcomeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserOutcome} or a {@link Page} of {@link UserOutcome} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserOutcomeQueryService extends QueryService<UserOutcome> {

    private final Logger log = LoggerFactory.getLogger(UserOutcomeQueryService.class);

    private final UserOutcomeRepository userOutcomeRepository;

    public UserOutcomeQueryService(UserOutcomeRepository userOutcomeRepository) {
        this.userOutcomeRepository = userOutcomeRepository;
    }

    /**
     * Return a {@link List} of {@link UserOutcome} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserOutcome> findByCriteria(UserOutcomeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserOutcome> specification = createSpecification(criteria);
        return userOutcomeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserOutcome} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserOutcome> findByCriteria(UserOutcomeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserOutcome> specification = createSpecification(criteria);
        return userOutcomeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserOutcomeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserOutcome> specification = createSpecification(criteria);
        return userOutcomeRepository.count(specification);
    }

    /**
     * Function to convert {@link UserOutcomeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserOutcome> createSpecification(UserOutcomeCriteria criteria) {
        Specification<UserOutcome> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserOutcome_.id));
            }
            if (criteria.getEndOutcomeOpponentA() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEndOutcomeOpponentA(), UserOutcome_.endOutcomeOpponentA));
            }
            if (criteria.getEndOutcomeOpponentB() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEndOutcomeOpponentB(), UserOutcome_.endOutcomeOpponentB));
            }
            if (criteria.getGameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGameId(), root -> root.join(UserOutcome_.game, JoinType.LEFT).get(Game_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UserOutcome_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(UserOutcome_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
