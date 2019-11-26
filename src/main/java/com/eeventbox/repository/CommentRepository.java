package com.eeventbox.repository;

import com.eeventbox.model.event.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByEventId(Long eventId);

	Optional<Comment> findByIdAndEventId(Long id, Long eventId);
}
