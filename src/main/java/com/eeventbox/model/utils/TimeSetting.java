package com.eeventbox.model.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for holding users' time availability settings.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class TimeSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection
    @MapKeyColumn(name="day")
    @CollectionTable(name="day_interval", joinColumns=@JoinColumn(name="time_settings_id"))
    private Map<Days, String> timeMap;
}