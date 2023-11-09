package org.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    @JsonCreator
    public Room(@JsonProperty("name")String name, @JsonProperty("capacity")int capacity, @JsonProperty("equipment")Map<String, Integer> equipment) {
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

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", equipment=" + equipment +
                '}';
    }
}
