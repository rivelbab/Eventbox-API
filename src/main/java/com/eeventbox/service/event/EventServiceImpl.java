package com.eeventbox.service.event;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.event.Comment;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;

    /**
     *==========================================================
     * 	Find one event by his id, used to display event details
     *==========================================================
     */
    public EventResponse findEvent(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));

        List<Comment> comments = commentService.listAllEventComments(eventId);

        EventResponse eventResponse = new EventResponse(event);
        eventResponse.setComments(comments);

        return eventResponse;
    }
    /**
     *==============================================================
     * 	Find one event by his id, used to update event in controller
     *==============================================================
     */
    public Optional<Event> findEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }
    /**
     *==========================================================
     * 				Create a new event
     *==========================================================
     */
    public Event createEvent(EventRequest eventRequest) {

        User organizer = userRepository.findById(eventRequest.getOrganizerId()).orElseThrow(() -> new AppException("User not exist."));

        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setCategory(eventRequest.getCategory());
        event.setDescription(eventRequest.getDescription());
        event.setLocation(eventRequest.getLocation());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setOrganizer(organizer);

        organizer.organizeNewEvent(event);

        Event newEvent = eventRepository.save(event);

        organizer.getAttendingEvents().add(newEvent);

        userRepository.save(organizer);

        return newEvent;
    }
    /**
     *==========================================================
     * 	    Find all past events, whatever the organizer
     *==========================================================
     */
    public List<Event> findPastEvents() {

        List<Event> allEvents = findEvents();
        LocalDate today = LocalDate.now();
        List<Event> pastEvents = new ArrayList<>();

        allEvents.forEach(event -> {
            if(event.getEndTime().toLocalDate().isBefore(today)){
                pastEvents.add(event);
            }
        });

        return pastEvents;
    }
    /**
     *==========================================================
     * 	    Find all future events, used in discover others menu
     *==========================================================
     */
    public List<Event> findFutureEvents(){

        List<Event> allEvents = findEvents();
        LocalDate today = LocalDate.now();
        List<Event> futureEvents = new ArrayList<>();

        allEvents.forEach(event -> {
            if(event.getEndTime().toLocalDate().isAfter(today)) {
                futureEvents.add(event);
            }
        });

        return futureEvents;
    }
    /**
     *==========================================================
     * 	Find all events, used to show all event or for stats
     * 	========================================================
     */
    public List<Event> findEvents() {

        return eventRepository.findAll();
    }
    /**
     * ===================================================
     * 	Update partially a user using PATCH Http method
     * 	==================================================
     */
    public void updateEvent(Event event) {eventRepository.save(event);}

    // =============== Utils methods used in others ========


}
