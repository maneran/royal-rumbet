package com.sheepit.app.repository;

import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.UserOutcome;
import com.sheepit.app.domain.UserOutcomeId;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserOutcome entity.
 */
@Repository
public interface UserOutcomeRepository extends JpaRepository<UserOutcome, UserOutcomeId>, JpaSpecificationExecutor<UserOutcome> {
    @Query("select userOutcome from UserOutcome userOutcome where userOutcome.gameId =:gameId")
    List<UserOutcome> findPartialWithEagerRelationships(@Param("gameId") Long gameId);
}
