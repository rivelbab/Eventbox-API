package com.eeventbox.service.event;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.event.Comment;
import com.eeventbox.model.user.User;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utility.Days;
import com.eeventbox.model.utility.Interest;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.UserRepository;

import com.eeventbox.service.comment.CommentService;
import com.eeventbox.utils.converter.Period;
import com.eeventbox.utils.converter.TimeSettingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventUserServiceImpl implements EventUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
	private CommentService commentService;

    public EventResponse getEventById(Long eventId) {

		Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));

		List<Comment> comments = commentService.listAllEventComments(eventId);

		EventResponse eventResponse = new EventResponse(event);
		eventResponse.setComments(comments);

		return eventResponse;
	}


	public EventResponse createEvent(EventRequest eventRequest) {

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

		EventResponse eventResponse = new EventResponse(newEvent);

		return eventResponse;
	}

	public void joinEvent(Long userId, Long eventId) {

		Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));
		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		user.enrolEvent(event);
		event.acceptAttendee(user);

		userRepository.save(user);
		eventRepository.save(event);
	}

    public List<Event> matchEventsForUser(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

        Set<Interest> interests = user.getInterests();
        List<Event> futureEvents = eventService.listFutureEvents();

        List<Period> periods = TimeSettingConverter.convertForTemplate(user.getTimeAvailability()).getPeriodsList();

        return futureEvents.stream().filter(
        		event -> interests.contains(event.getCategory())).filter(e -> {
                    Period weekDay = periods.get(Days.valueOf(e.getEndTime().getDayOfWeek().name()).ordinal());
                    return e.getStartTime().toLocalTime().isAfter(weekDay.getStart()) && e.getEndTime().toLocalTime().isBefore(weekDay.getEnd());
                }).collect(Collectors.toList());
    }

	public List<Event> listUserEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents();
	}

	public List<Event> listUserFutureEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
	}

	public List<Event> listUserPastEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());
	}
}
