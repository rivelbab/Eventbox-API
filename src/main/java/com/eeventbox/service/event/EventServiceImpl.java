package com.eeventbox.service.event;

import com.eeventbox.model.event.Event;
import com.eeventbox.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

public class EventServiceImpl {

    @Autowired
    private EventRepository eventRepository;
}
