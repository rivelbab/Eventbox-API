package com.eeventbox.repository;

import com.eeventbox.model.event.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Integer> {

    List<Location> findAll();
    Location findByName(String name);
}
