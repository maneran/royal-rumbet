package com.sheepit.app.repository;

import com.sheepit.app.domain.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Group entity.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    @Query(
        value = "select distinct jhiGroup from Group jhiGroup left join fetch jhiGroup.users",
        countQuery = "select count(distinct jhiGroup) from Group jhiGroup"
    )
    Page<Group> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct jhiGroup from Group jhiGroup left join fetch jhiGroup.users")
    List<Group> findAllWithEagerRelationships();

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.users where jhiGroup.id =:id")
    Optional<Group> findOneWithEagerRelationships(@Param("id") Long id);
}
