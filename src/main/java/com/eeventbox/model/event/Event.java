package com.eeventbox.model.event;

import com.eeventbox.model.user.User;
import com.eeventbox.model.utils.Interest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * ==============================================================================================================
 * This model class holds information about event. This includes data about attendees, start and end
 * time and other relevant information that can be persisted in DB and also be registered on the Google Calendar.
 * Created by Rivel babindamana on 29/10/2019, 15:40 at Paris
 * ==============================================================================================================
 */

@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    private String title;
    private String description;

    @ManyToOne
    private User organizer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Interest category;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> confirmedAttendees;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> pendingAttendees;

    public void acceptAttendee(User attendee) {
        if (pendingAttendees.contains(attendee)) {
            pendingAttendees.remove(attendee);
            this.confirmedAttendees.add(attendee);
            attendee.attendEvent(this);
        }
    }

    public void acceptEnrollment(User attendee) {
        this.pendingAttendees.add(attendee);
    }
}
