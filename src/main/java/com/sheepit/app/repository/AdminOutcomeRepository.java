package com.sheepit.app.repository;

import com.sheepit.app.domain.AdminOutcome;
import com.sheepit.app.domain.AdminOutcomeId;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AdminOutcome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminOutcomeRepository extends JpaRepository<AdminOutcome, AdminOutcomeId>, JpaSpecificationExecutor<AdminOutcome> {}
