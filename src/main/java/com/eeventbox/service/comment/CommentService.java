package com.eeventbox.service.comment;

import com.eeventbox.model.event.Comment;
import com.eeventbox.payload.event.CommentRequest;

import java.util.List;

public interface CommentService {

	List<Comment> listAllEventComments(Long eventId);

	Comment createComment(CommentRequest commentRequest);

	Comment updateComment(CommentRequest commentRequest);

	void deleteComment(CommentRequest commentRequest);
}
