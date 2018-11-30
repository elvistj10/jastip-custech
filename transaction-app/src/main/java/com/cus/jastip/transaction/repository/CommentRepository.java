package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.Comment;
import com.cus.jastip.transaction.domain.Posting;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPosting(Posting posting, Pageable pageable);

}
