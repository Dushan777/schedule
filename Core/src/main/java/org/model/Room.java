package org.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    public static Room getRoomByName(String name, ScheduleSpecification schedule) throws NullPointerException
    {
        for(Room r : schedule.getRooms())
        {
            if(r.getName().equals(name))
                return r;
        }
        throw new NullPointerException("Room not found!");
    }

    public static Map<String, String> getAdditionalDataByTerm(Term term, ScheduleSpecification schedule, String weekDay) throws NullPointerException
    {
        LocalDate start = term.getTime().getStartDate();
        LocalDate end = term.getTime().getEndDate();
        while(!start.isAfter(end)) {
            if(start.isBefore(end) && !Time.getWeekDay(start).equals(weekDay))
            {
                start = start.plusDays(1);
                continue;
            }
            Term term1 = new Term(term.getRoom(), new Time(start, start, term.getTime().getStartTime(), term.getTime().getEndTime()), null);
            for (Term t : schedule.getTerms()) {
                if (t.equals(term1))
                    return t.getAdditionalData();
            }
            start = start.plusDays(7);
        }

        throw new NullPointerException("Term not found!");
    }
}
