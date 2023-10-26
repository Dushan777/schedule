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

    @Override
    public boolean equals(Object obj) {
        Term term = (Term) obj;
        return this.getRoom().equals(term.getRoom()) && this.getTime().equals(term.getTime());
    }
}
