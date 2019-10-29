package com.eeventbox.repository;

import com.eeventbox.model.event.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {

    List<Event> findAll(Sort sort);

    Event findByTitle(String title);
}
