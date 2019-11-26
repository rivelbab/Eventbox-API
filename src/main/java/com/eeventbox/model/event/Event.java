package com.eeventbox.model.event;
/**
 * ================================================================
 * This model class holds information about event. This includes
 * data about attendees, start and end time and other relevant
 * information that can be persisted in DB and also be registered
 * on the Google Calendar.
 * Created by Rivel babindamana on 29/10/2019, 15:40 at Paris
 * ================================================================
 */
import com.eeventbox.model.user.User;
import com.eeventbox.model.utility.AuditModel;
import com.eeventbox.model.utility.Interest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int likeCount;
    private int unlikeCount;
    private int shareCount;
    private int commentCount;

    @ManyToOne
    private User organizer;

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
