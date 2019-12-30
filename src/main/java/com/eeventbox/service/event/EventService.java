package com.eeventbox.service.event;
/**
 * ================================================
 * Contains all methods to deal with events resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utility.Interest;
import com.eeventbox.payload.event.EventResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {

    EventResponse findEvent(Long eventId);

    Optional<Event> findEventById(Long eventId);

    Event createEvent(MultipartFile file, String title, String desc, String location, LocalDateTime startTime, LocalDateTime endTime, Set<Interest> category, Long organizerId );

    void addEventImage(MultipartFile file, Long eventId);

    List<Event> findPastEvents();

    List<Event> findFutureEvents();

    List<Event> findEvents();

    void updateEvent(Event event);

    void joinEvent(Long userId, Long eventId);

    List<Event> matchEventsForUser(Long userId);

    List<Event> findUserEvents(Long userId);

    List<Event> findUserFutureEvents(Long userId);

    List<Event> findUserPastEvents(Long userId);
}
