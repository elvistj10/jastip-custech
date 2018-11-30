package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.Comment;
import com.cus.jastip.transaction.domain.SubComment;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the SubComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
	List<SubComment> findByComment(Comment comment, Pageable pageable);
}
