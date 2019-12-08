package com.eeventbox.service.event;
/**
 * =========================================================
 * This service class provides methods to do operations
 * which are tightly related between Event and User
 * Created by Rivel babindamana on 31/10/2019 at Nanterre U
 * =========================================================
 */
import com.eeventbox.model.event.Event;

import java.util.List;

public interface EventUserService {

	void joinEvent(Long userId, Long eventId);

	List<Event> matchEventsForUser(Long userId);

	List<Event> findUserEvents(Long userId);

	List<Event> findUserFutureEvents(Long userId);

	List<Event> findUserPastEvents(Long userId);
}
