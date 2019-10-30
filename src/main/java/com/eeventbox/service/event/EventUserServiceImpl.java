package com.eeventbox.service.event;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utils.Days;
import com.eeventbox.model.utils.Interest;
import com.eeventbox.repository.EventRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.user.UserService;

import com.eeventbox.utils.converter.Period;
import com.eeventbox.utils.converter.TimeSettingConverter;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventUserServiceImpl {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    public List<Event> matchEventsForUser(User user) {
        Set<Interest> interests = user.getInterests();
        List<Event> futureEvents = eventService.listOfFutureEvents();
        List<Period> periods = TimeSettingConverter.convertForTemplate(user.getTimeAvailability()).getPeriodsList();
        return futureEvents.stream()
                .filter(event -> interests.contains(event.getCategory()))
                .filter(e -> {
                    Period weekDay = periods.get(Days.valueOf(e.getEndTime().getDayOfWeek().name()).ordinal());
                    return e.getStartTime().toLocalTime().isAfter(weekDay.getStart()) && e.getEndTime().toLocalTime().isBefore(weekDay.getEnd());
                })
                .collect(Collectors.toList());
    }

    public void joinEvent(User user, Integer eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException("Event not set."));
        user.enrolEvent(event);
        event.acceptAttendee(user);
        userRepository.save(user);
        eventRepository.save(event);
    }

    public Event createEvent(User user, Event event) {
        if (timeSlotIsFree(event)) {
            user.organizeNewEvent(event);
            if (!createEventOnCalendar(event)) {
                return null;
            }
            Event newEvent = eventRepository.save(event);
            userRepository.save(user);
            return newEvent;
        }
        return null;
    }

    private boolean createEventOnCalendar(Event event) {
        String calendarID = event.getLocation().getCalendarID();
        com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
                .setSummary(event.getTitle())
                .setLocation(event.getLocation().getAddress())
                .setDescription(event.getDescription());

        DateTime startDate = new DateTime(event.getStartTime().toString() + ":00+03:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDate)
                .setTimeZone("Europe/Paris");
        googleEvent.setStart(start);

        DateTime endDate = new DateTime(event.getEndTime().toString() + ":00+03:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDate)
                .setTimeZone("Europe/Paris");
        googleEvent.setEnd(end);

        try {
            googleEvent = getCalendar().events().insert(calendarID, googleEvent).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.printf("Event created: %s\n", googleEvent.getHtmlLink());

        return true;
    }

    /**
     * Makes a Free/Busy query on Google Calendar to check if the associated {@link com.eeventbox.model.event.Location}
     * of the requested {@link Event} is free, during the start and end time of the event.
     * @param event
     * @return true, if time is free. False, if the associated location of the event is (partly)
     * busy during the start-end time of the event.
     */
    private boolean timeSlotIsFree(Event event) {
        String calendarID = event.getLocation().getCalendarID();
        FreeBusyRequest fbrq = new FreeBusyRequest();
        DateTime startDate = new DateTime(event.getStartTime().toString() + ":00+03:00");
        DateTime endTime = new DateTime(event.getEndTime().toString() + ":00+03:00");

        fbrq.setTimeMin(startDate);
        fbrq.setTimeMax(endTime);
        FreeBusyRequestItem fbrqItem = new FreeBusyRequestItem();
        fbrqItem.setId(calendarID);
        List<FreeBusyRequestItem> listOffbrqItems = new ArrayList<FreeBusyRequestItem>();
        listOffbrqItems.add(fbrqItem);

        fbrq.setItems(listOffbrqItems);

        Calendar.Freebusy.Query fbq = null;
        FreeBusyResponse fbResponse = null;
        try {
            fbq = getCalendar().freebusy().query(fbrq);
            fbResponse = fbq.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(fbResponse.toString());

        StringBuilder busyPeriods = new StringBuilder("");
        for (TimePeriod period : fbResponse.getCalendars().get(calendarID).getBusy()) {
            busyPeriods.append(period.toString());
        }

        if (busyPeriods.length() < 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Environmental variable SERVICE_ACCOUNT_ID should be defined. User should get it from Google API Manager.
     * @return {@link com.google.api.services.calendar.Calendar}
     */
    private Calendar getCalendar() {
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        HttpTransport HTTP_TRANSPORT = null;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<String> scope = Collections.singleton(CalendarScopes.CALENDAR);
        FileInputStream auth_credentials = null;
        try {
            auth_credentials = new FileInputStream("src/main/resources/Events4me.p12");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GoogleCredential credentials = null;
        try {
            credentials = createCredentialForServiceAccount(HTTP_TRANSPORT, JSON_FACTORY, System.getenv("SERVICE_ACCOUNT_ID"), scope, auth_credentials);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName("Events4ME").build();
        return service;
    }

    /**
     * Google's service account credentials are encrypted in file resources/Events4me.p12. To decrypt them, environmental vairable
     * P12_PASSWORD should be defined. It's password for accessing the Events4me.p12.
     *
     * @return GoogleCredential, which is Google-specific implementation of the OAuth 2.0 helper for accessing the calendars
     * asscoiated with {@link com.eeventbox.model.event.Location}
     */

    public GoogleCredential createCredentialForServiceAccount(
            HttpTransport transport,
            JsonFactory jsonFactory,
            String serviceAccountId,
            Collection<String> serviceAccountScopes, InputStream p12file) throws GeneralSecurityException, IOException {


        String p12Password = System.getenv("P12_PASSWORD");
        KeyStore keystore = KeyStore.getInstance("PKCS12");

        keystore.load(p12file, p12Password.toCharArray());
        PrivateKey key = (PrivateKey) keystore.getKey("privatekey", p12Password.toCharArray());

        return new GoogleCredential.Builder().setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(serviceAccountId)
                .setServiceAccountScopes(serviceAccountScopes)
                .setServiceAccountPrivateKey(key)
                .build();
    }
}
