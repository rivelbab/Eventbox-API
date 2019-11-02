package com.eeventbox.repository;

import com.eeventbox.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAll();

    Event findByTitle(String title);

    Optional<Event> findById(Long id);
}
