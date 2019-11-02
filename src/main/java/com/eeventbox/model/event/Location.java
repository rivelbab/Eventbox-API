package com.eeventbox.model.event;
/**
 * ==========================================================
 * This model contains utility infos about event location.
 * Created by Rivel babindamana on 29/10/2019, 15:40 at Paris
 * ==========================================================
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private Set<Event> events;
}
