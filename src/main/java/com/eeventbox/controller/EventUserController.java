package com.eeventbox.controller;

import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;
import com.eeventbox.service.event.EventUserService;
import com.eeventbox.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ================================================
 * Contains all endpoints for the event resources
 * Created by Rivelbab on 29/11/2019 at Nanterre U.
 * ================================================
 */
@RestController
@RequestMapping("/v1")
public class EventUserController {

	@Autowired
	private EventUserService eventUserService;
	@Autowired
	private UserService userService;

	@ResponseBody
	@GetMapping("/events/{userId}/matchs")
	public List<Event> getMatchedEventsForUser(@PathVariable("userId") Long userId) {
		return eventUserService.matchEventsForUser(userId);
	}

	@PostMapping("/events/{eventId}/join")
	public ResponseEntity<?> joinEvent(@PathVariable("eventId") Long eventId, @RequestParam("userId") Long userId) {
		eventUserService.joinEvent(userId, eventId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("events/{userId}/all")
	public List<Event> listUserEvent(@PathVariable("userId") Long userId) {
		return eventUserService.listUserEvents(userId);
	}

	@PostMapping("/events")
	public EventResponse createEvent(@RequestBody EventRequest eventRequest) {
		return eventUserService.createEvent(eventRequest);
	}

	@GetMapping("/events/{eventId}")
	public EventResponse showEvent(@PathVariable("eventId") Long eventid) {
		return eventUserService.getEventById(eventid);
	}
}
