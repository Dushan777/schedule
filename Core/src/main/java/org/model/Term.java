package org.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Term {

    private Room room;
    private Time time;
    private Map<String, String> additionalData;   // tip, vrednost npr: grupa, 301a. da bi se znalo sta taj podatak predstavlja

    public Term(Room room, Time time, Map<String, String> additionalData) {
        this.room = room;
        this.time = time;
        this.additionalData = (additionalData != null) ? additionalData : new HashMap<>();
    }
    public Term()
    {
        additionalData = new HashMap<>();
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Term))
            return false;
        Term term = (Term) obj;
        return this.getRoom().equals(term.getRoom()) && this.getTime().equals(term.getTime());
    }

    @Override
    public String toString() {
        return "Term{" +
                "room=" + room +
                ", time=" + time +
                ", additionalData=" + additionalData +
                '}';
    }

    public boolean hasNULL()
    {
        return room.getName() == null || Objects.equals(room.getName(), "") || room.getCapacity() <= 0 || time.hasNULL();
    }

}
