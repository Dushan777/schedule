package org.model;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class Room {

    private String name;
    private int capacity;   // kapacitet prostorije
    private Map<String, Integer> equipment; // prvo ide stvar pa njena kolicina

    public Room(String name, int capacity, Map<String, Integer> equipment) {
        this.name = name;
        this.capacity = capacity;
        this.equipment = (equipment != null) ? equipment : new HashMap<>();;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Room))
            return false;
        return this.name.equals(((Room)obj).name);
    }
}
