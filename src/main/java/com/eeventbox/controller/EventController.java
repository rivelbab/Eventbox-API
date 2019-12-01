package com.eeventbox.controller;

import com.eeventbox.model.event.Event;
import com.eeventbox.service.event.EventService;
import com.eeventbox.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ================================================
 * Contains all endpoints for the event resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
@RestController
@RequestMapping("/v1")
public class EventController {

	@Autowired
	private EventService eventService;
	@Autowired
	private UserService userService;

	@GetMapping("/events")
	public @ResponseBody List<Event> listAllEvents() {
		return eventService.listAllEvents();
	}

	@GetMapping("/events/past")
	public @ResponseBody List<Event> listPastEvents() {
		return eventService.listPastEvents();
	}

	@GetMapping("/events/future")
	public @ResponseBody List<Event> listFutureEvents() {
		return eventService.listFutureEvents();
	}

}
