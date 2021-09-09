package com.sheepit.app.repository;

import com.sheepit.app.domain.Game;
import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GameConfig entity.
 */
@Repository
public interface GameConfigRepository extends JpaRepository<GameConfig, Long>, JpaSpecificationExecutor<GameConfig> {
    @Query("select gameConfig from GameConfig gameConfig where gameConfig.gameType =:gameType and gameConfig.gameStageType =:gameStageType")
    List<GameConfig> findPartialWithEagerRelationships(
        @Param("gameType") GameType gameType,
        @Param("gameStageType") GameStageType gameStageType
    );

    @Query("select gameConfig from GameConfig gameConfig where gameConfig.id =:id")
    Optional<GameConfig> findOneWithEagerRelationships(@Param("id") Long id);
}
