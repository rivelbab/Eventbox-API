package com.eeventbox.service.comment;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.event.Comment;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.event.CommentRequest;
import com.eeventbox.repository.CommentRepository;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;

	public List<Comment> listAllEventComments(Long eventId) {

		return commentRepository.findByEventId(eventId);
	}

	public Comment createComment(CommentRequest commentRequest) {

		Event event = eventRepository.findById(commentRequest.getEventId()).orElseThrow(() -> new AppException("Event not exist."));
		User author = userRepository.findById(commentRequest.getAuthorId()).orElseThrow(() -> new AppException("User not exist."));

		Comment comment = new Comment();

		comment.setContent(commentRequest.getContent());
		comment.setAuthor(author);
		comment.setEvent(event);
		commentRepository.save(comment);

		return comment;
	}

	public void updateComment(Long commentId, CommentRequest commentRequest) {

		Event event = eventRepository.findById(commentRequest.getEventId()).orElseThrow(() -> new AppException("Event not exist."));
		User author = userRepository.findById(commentRequest.getAuthorId()).orElseThrow(() -> new AppException("User not exist."));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException("Comment not exist."));

		comment.setContent(commentRequest.getContent());
		comment.setAuthor(author);
		comment.setEvent(event);
		commentRepository.save(comment);
	}

	public void deleteComment(Long commentId, Long eventId) {

		Comment comment = commentRepository.findByIdAndEventId(commentId, eventId)
				.orElseThrow(() -> new AppException("Comment not exist."));

		commentRepository.delete(comment);
	}
}
