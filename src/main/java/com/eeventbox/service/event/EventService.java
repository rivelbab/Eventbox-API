package com.eeventbox.service.event;

import com.eeventbox.model.event.Event;

import java.util.List;

public interface EventService {

    List<Event> listOfAllEvents();

    List<Event> listOfPastEvents();

    List<Event> listOfFutureEvents();

    void setLocationForEvent(Integer eventId, Integer locationId);
}
