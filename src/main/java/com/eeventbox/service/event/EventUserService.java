package com.eeventbox.service.event;
/**
 * =========================================================
 * This service class provides methods to do operations
 * which are tightly related between Event and User
 * Created by Rivel babindamana on 31/10/2019 at Nanterre U
 * =========================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;

import java.util.List;

public interface EventUserService {

	EventResponse getEventById(Long eventId);

	EventResponse createEvent(EventRequest eventRequest);

	void joinEvent(Long userId, Long eventId);

	List<Event> matchEventsForUser(Long userId);

	List<Event> listUserEvents(Long userId);

	List<Event> listUserFutureEvents(Long userId);

	List<Event> listUserPastEvents(Long userId);
}
