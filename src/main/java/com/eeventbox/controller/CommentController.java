package com.eeventbox.controller;
/**
 * ===================================================
 * Contains all endpoints to deal with event comments
 * Created by Rivelbab on 8/12/2019 at Nanterre U.
 * ===================================================
 */
import com.eeventbox.model.event.Comment;
import com.eeventbox.payload.event.CommentRequest;
import com.eeventbox.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController {

	@Autowired
	private CommentService commentService;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createComment(@Valid @RequestBody CommentRequest commentRequest) {

		Comment commentCreated = commentService.createComment(commentRequest);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(commentCreated.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentRequest commentRequest) {
		commentService.updateComment(commentId, commentRequest);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(path = "/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestBody Long eventId) {
		commentService.deleteComment(commentId, eventId);

		return ResponseEntity.noContent().build();
	}
}
