package com.eeventbox.service.comment;

import com.eeventbox.model.event.Comment;
import com.eeventbox.payload.event.CommentRequest;

import java.util.List;

public interface CommentService {

	List<Comment> listAllEventComments(Long eventId);

	Comment createComment(CommentRequest commentRequest);

	void updateComment(Long commentId, CommentRequest commentRequest);

	void deleteComment(Long commentId, Long eventId);
}
