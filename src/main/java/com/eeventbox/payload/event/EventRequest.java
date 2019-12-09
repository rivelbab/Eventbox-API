package com.eeventbox.payload.event;

import com.eeventbox.model.utility.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequest {

	private String title;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Interest category;
	private Long organizerId;
	private String location;
}
