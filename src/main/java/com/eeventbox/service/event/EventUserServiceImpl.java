package com.eeventbox.service.event;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utility.Days;
import com.eeventbox.model.utility.Interest;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.UserRepository;
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

	/**
	 * ==========================================================
	 * 	Join a given event, used to become a event's participant
	 * 	=========================================================
	 */
	public void joinEvent(Long userId, Long eventId) {

		Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));
		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		user.enrolEvent(event);
		event.acceptAttendee(user);

		userRepository.save(user);
		eventRepository.save(event);
	}

	/**
	 * ========================================================
	 * Find all future event that match with the user interests
	 * ========================================================
	 */
    public List<Event> matchEventsForUser(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

        Set<Interest> interests = user.getInterests();
        List<Event> futureEvents = eventService.findFutureEvents();

        List<Period> periods = TimeSettingConverter.convertForTemplate(user.getTimeAvailability()).getPeriodsList();

        return futureEvents.stream().filter(
        		event -> interests.contains(event.getCategory())).filter(e -> {
                    Period weekDay = periods.get(Days.valueOf(e.getEndTime().getDayOfWeek().name()).ordinal());
                    return e.getStartTime().toLocalTime().isAfter(weekDay.getStart()) && e.getEndTime().toLocalTime().isBefore(weekDay.getEnd());
                }).collect(Collectors.toList());
    }
	/**
	 * ==========================================================
	 * Find all event created by this user, future and past event
	 * ==========================================================
	 */
	public List<Event> findUserEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents();
	}
	/**
	 * ==========================================================
	 * Find all event created by this user, future event only
	 * ==========================================================
	 */
	public List<Event> findUserFutureEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
	}
	/**
	 * ==========================================================
	 * Find all event created by this user, past event only
	 * ==========================================================
	 */
	public List<Event> findUserPastEvents(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());
	}
}
