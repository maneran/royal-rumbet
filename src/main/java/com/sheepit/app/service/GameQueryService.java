package com.sheepit.app.service;

import com.sheepit.app.domain.*; // for static metamodels
import com.sheepit.app.domain.Game;
import com.sheepit.app.repository.GameRepository;
import com.sheepit.app.service.criteria.GameCriteria;
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
 * Service for executing complex queries for {@link Game} entities in the database.
 * The main input is a {@link GameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Game} or a {@link Page} of {@link Game} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GameQueryService extends QueryService<Game> {

    private final Logger log = LoggerFactory.getLogger(GameQueryService.class);

    private final GameRepository gameRepository;

    public GameQueryService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Return a {@link List} of {@link Game} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Game> findByCriteria(GameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Game} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Game> findByCriteria(GameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.count(specification);
    }

    /**
     * Function to convert {@link GameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Game> createSpecification(GameCriteria criteria) {
        Specification<Game> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Game_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Game_.name));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Game_.dateStart));
            }
            if (criteria.getGameType() != null) {
                specification = specification.and(buildSpecification(criteria.getGameType(), Game_.gameType));
            }
            if (criteria.getStageType() != null) {
                specification = specification.and(buildSpecification(criteria.getStageType(), Game_.stageType));
            }
            if (criteria.getOpponentAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOpponentAId(), root -> root.join(Game_.opponentA, JoinType.LEFT).get(Opponent_.id))
                    );
            }
            if (criteria.getOpponentBId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOpponentBId(), root -> root.join(Game_.opponentB, JoinType.LEFT).get(Opponent_.id))
                    );
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(Game_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Game_.users, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
