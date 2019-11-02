package com.eeventbox.service.event;
/**
 * ================================================
 * Contains all useful operations for all Users
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;

import java.util.List;

public interface EventService {

    List<Event> listAllEvents();

    List<Event> listPastEvents();

    List<Event> listFutureEvents();

    void setLocationForEvent(Long eventId, Integer locationId);
}
