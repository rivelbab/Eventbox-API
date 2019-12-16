package com.eeventbox.payload.event;

import com.eeventbox.model.event.Comment;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;
import com.eeventbox.model.utility.Interest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EventResponse {

	private Long id;
	private String title;
	private String description;
	private String location;
	private String imageName;
	private String imageUri;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Set<Interest> category;
	private User organizer;

	private int likeCount;
	private int unlikeCount;
	private int shareCount;
	private int commentCount;

	private Set<User> confirmedAttendees;
	private Set<User> pendingAttendees;

	private List<Comment> comments;

	public EventResponse(Event event) {

		this.id = event.getId();
		this.title = event.getTitle();
		this.description = event.getDescription();
		this.location = event.getLocation();
		this.startTime = event.getStartTime();
		this.endTime = event.getEndTime();
		this.category = event.getCategory();
		this.organizer = event.getOrganizer();
		this.likeCount = event.getLikeCount();
		this.unlikeCount = event.getUnlikeCount();
		this.shareCount = event.getShareCount();
		this.commentCount = event.getCommentCount();
		this.confirmedAttendees = event.getConfirmedAttendees();
		this.pendingAttendees = event.getPendingAttendees();
		this.imageName = event.getImageName();
		this.imageUri = event.getImageUri();
	}
}
