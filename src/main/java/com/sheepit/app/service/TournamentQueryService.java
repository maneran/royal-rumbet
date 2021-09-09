package com.sheepit.app.service;

import com.sheepit.app.domain.*; // for static metamodels
import com.sheepit.app.domain.Tournament;
import com.sheepit.app.repository.TournamentRepository;
import com.sheepit.app.service.criteria.TournamentCriteria;
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
 * Service for executing complex queries for {@link Tournament} entities in the database.
 * The main input is a {@link TournamentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tournament} or a {@link Page} of {@link Tournament} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TournamentQueryService extends QueryService<Tournament> {

    private final Logger log = LoggerFactory.getLogger(TournamentQueryService.class);

    private final TournamentRepository tournamentRepository;

    public TournamentQueryService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Return a {@link List} of {@link Tournament} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tournament> findByCriteria(TournamentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tournament} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tournament> findByCriteria(TournamentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TournamentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentRepository.count(specification);
    }

    /**
     * Function to convert {@link TournamentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tournament> createSpecification(TournamentCriteria criteria) {
        Specification<Tournament> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tournament_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tournament_.name));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Tournament_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), Tournament_.dateEnd));
            }
            if (criteria.getRegistrationEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegistrationEndDate(), Tournament_.registrationEndDate));
            }
            if (criteria.getGameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGameId(), root -> root.join(Tournament_.games, JoinType.LEFT).get(Game_.id))
                    );
            }
        }
        return specification;
    }
}
