package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.Updates;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Updates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpdatesRepository extends JpaRepository<Updates, Long> {

}
