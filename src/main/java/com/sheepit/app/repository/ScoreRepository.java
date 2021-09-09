package com.sheepit.app.repository;

import com.sheepit.app.domain.Score;
import com.sheepit.app.domain.ScoreId;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Score entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreRepository extends JpaRepository<Score, ScoreId>, JpaSpecificationExecutor<Score> {}
