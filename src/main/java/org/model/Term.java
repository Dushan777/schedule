package org.model;

import java.util.*;

public class Term {

    private Room room;
    private Time time;
    private Map<String, String> additionalData = new HashMap<>();   // tip, vrednost npr: grupa, 301a. da bi se znalo sta taj podatak predstavlja

    public Term(Room room, Time time) {
        this.room = room;
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        Term term = (Term) obj;
        if(term.time.equals(this.time))
        {
            return term.room.equals(this.room);
        }
        return false;
    }

    public Room getRoom() {
        return room;
    }

    public Time getTime() {
        return time;
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }
}
