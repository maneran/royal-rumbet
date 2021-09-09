package com.sheepit.app.repository;

import com.sheepit.app.domain.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    @Query(
        value = "select distinct game from Game game left join fetch game.users",
        countQuery = "select count(distinct game) from Game game"
    )
    Page<Game> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct game from Game game left join fetch game.users")
    List<Game> findAllWithEagerRelationships();

    @Query("select game from Game game left join fetch game.users where game.id =:id")
    Optional<Game> findOneWithEagerRelationships(@Param("id") Long id);
}
