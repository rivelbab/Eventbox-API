package com.eeventbox.payload.event;

import com.eeventbox.model.utility.Interest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequest {
	private String title;
	private String description;
	private String location;
	private Set<Interest> category;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Long organizerId;
}
