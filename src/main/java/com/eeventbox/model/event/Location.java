package com.eeventbox.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private int id;

    private String name;
    private String address;

    private Set<Event> events;

    private String calendarID;
}
