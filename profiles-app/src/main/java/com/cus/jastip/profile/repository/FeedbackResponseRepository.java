package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.FeedbackResponse;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FeedbackResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {

}
