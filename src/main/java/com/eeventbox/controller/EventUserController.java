package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the event resources
 * Created by Rivelbab on 29/11/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventResponse;
import com.eeventbox.service.event.EventUserService;
import com.eeventbox.service.user.UserService;
import com.eeventbox.utils.helper.PatchHelper;
import com.eeventbox.utils.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users/events")
@RequiredArgsConstructor
public class EventUserController {

	@Autowired
	private EventUserService eventUserService;
	@Autowired
	private UserService userService;

	private final EventMapper mapper;

	private final PatchHelper patchHelper;

	@PostMapping(path = "/{eventId}/join", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> joinEvent(@PathVariable Long eventId, @RequestBody Long userId) {
		eventUserService.joinEvent(userId, eventId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(path = "/{userId}/matchs", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> matchEvents(@PathVariable Long userId) {

		List<Event> events = eventUserService.matchEventsForUser(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserEvents(@PathVariable Long userId) {

		List<Event> events = eventUserService.findUserEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/future", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserFutureEvents(@PathVariable Long userId) {

		List<Event> events = eventUserService.findUserFutureEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/past", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserPastEvents(@PathVariable Long userId) {

		List<Event> events = eventUserService.findUserPastEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}
}
