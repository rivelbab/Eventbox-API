package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the event resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.exception.AppException;
import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;
import com.eeventbox.service.event.EventService;
import com.eeventbox.utils.helper.PatchHelper;
import com.eeventbox.utils.mapper.EventMapper;
import com.eeventbox.utils.web.PatchMediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {

	@Autowired
	private EventService eventService;

	private final EventMapper mapper;

	private final PatchHelper patchHelper;


	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findEvents() {
		List<Event> events = eventService.findEvents();
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return ResponseEntity.ok(eventResponses);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public 	ResponseEntity<Void> createEvent(@Valid @RequestBody EventRequest eventRequest) {

		Event eventCreated = eventService.createEvent(eventRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(eventCreated.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping(path = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EventResponse> findEventById(@PathVariable Long eventId) {

		EventResponse eventResponse = eventService.findEvent(eventId);
		return ResponseEntity.ok(eventResponse);
	}

	@PatchMapping(path = "/eventId", consumes = PatchMediaType.APPLICATION_MERGE_PATCH_VALUE)
	public ResponseEntity<Void> updateEvent(@PathVariable Long eventId, @RequestBody JsonMergePatch mergePatchDocument) {

		Event event = eventService.findEventById(eventId).orElseThrow(() -> new AppException("Event not set."));
		EventRequest eventRequest = mapper.asEventRequest(event);
		EventRequest eventRequestPatched = patchHelper.mergePatch(mergePatchDocument, eventRequest, EventRequest.class);
		mapper.update(event, eventRequestPatched);
		eventService.updateEvent(event);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(path = "/past", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findPastEvents() {

		List<Event> events = eventService.findPastEvents();
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/future", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findFutureEvents() {

		List<Event> events = eventService.findFutureEvents();
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return ResponseEntity.ok(eventResponses);
	}

	@PostMapping(path = "/{eventId}/join", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> joinEvent(@PathVariable Long eventId, @RequestBody Long userId) {
		eventService.joinEvent(userId, eventId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(path = "/{userId}/matchs", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> matchEvents(@PathVariable Long userId) {

		List<Event> events = eventService.matchEventsForUser(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserEvents(@PathVariable Long userId) {

		List<Event> events = eventService.findUserEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/future", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserFutureEvents(@PathVariable Long userId) {

		List<Event> events = eventService.findUserFutureEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}

	@GetMapping(path = "/{userId}/past", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventResponse>> findUserPastEvents(@PathVariable Long userId) {

		List<Event> events = eventService.findUserPastEvents(userId);
		List<EventResponse> eventResponses = mapper.asEventResponse(events);

		return  ResponseEntity.ok(eventResponses);
	}
}
