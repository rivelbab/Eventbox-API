package com.eeventbox.repository;

import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAll();

    Event findByTitle(String title);

    Optional<Event> findById(Long id);

    Optional<List<Event>> findByOrganizer(User organizer);
}
