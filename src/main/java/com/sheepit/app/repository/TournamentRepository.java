package com.sheepit.app.repository;

import com.sheepit.app.domain.Tournament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tournament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {}
