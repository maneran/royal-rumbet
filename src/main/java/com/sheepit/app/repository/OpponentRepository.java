package com.sheepit.app.repository;

import com.sheepit.app.domain.Opponent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Opponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpponentRepository extends JpaRepository<Opponent, Long>, JpaSpecificationExecutor<Opponent> {}
