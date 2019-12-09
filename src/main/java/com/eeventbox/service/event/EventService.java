package com.eeventbox.service.event;
/**
 * ================================================
 * Contains all methods to deal with events resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;

import java.util.List;
import java.util.Optional;

public interface EventService {

    EventResponse findEvent(Long eventId);

    Optional<Event> findEventById(Long eventId);

    Event createEvent(EventRequest eventRequest);

    void updateEvent(Event event);

    List<Event> findPastEvents();

    List<Event> findFutureEvents();

    List<Event> findEvents();
}
