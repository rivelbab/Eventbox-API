package com.eeventbox.service.event;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.event.Location;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;

    public List<Event> listAllEvents() {

        return eventRepository.findAll();
    }

    public List<Event> listPastEvents() {

        List<Event> allEvents = listAllEvents();
        LocalDate today = LocalDate.now();
        List<Event> pastEvents = new ArrayList<>();

        allEvents.forEach(event -> {
            if(event.getEndTime().toLocalDate().isBefore(today)){
                pastEvents.add(event);
            }
        });

        return pastEvents;
    }

    public List<Event> listFutureEvents(){

        List<Event> allEvents = listAllEvents();
        LocalDate today = LocalDate.now();
        List<Event> futureEvents = new ArrayList<>();

        allEvents.forEach(event -> {
            if(event.getEndTime().toLocalDate().isAfter(today)) {
                futureEvents.add(event);
            }
        });

        return futureEvents;
    }

    public void setLocationForEvent(Long eventId, Integer locationId) {

        if((eventId >= 0) && (locationId >= 0)) {

            Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));
            Location location = locationRepository.findById(locationId).orElseThrow(() -> new AppException("Location not set."));

            event.setLocation(location);
            eventRepository.save(event);
            locationRepository.save(location);
        }
    }
}
