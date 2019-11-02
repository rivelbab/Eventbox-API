package com.eeventbox.payload.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

	private Long id;
	private Long eventId;
	private String content;
	private Long authorId;
}
