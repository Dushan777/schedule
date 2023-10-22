package org.model;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private String name;
    private int capacity;   // kapacitet prostorije
    private Map<String, Integer> equipment = new HashMap<>(); // prvo ide stvar pa njena kolicina

    public Room(String name, int capacity, Map<String, Integer> equipment) {
        this.name = name;
        this.capacity = capacity;
        this.equipment = equipment;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Map<String, Integer> getEquipment() {
        return equipment;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Room)obj).name);
    }
}
