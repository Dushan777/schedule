package org.example;

import java.util.*;

public class Term {

    private Room room;
    private Time time;
    private Map<String, String> additionalData = new HashMap<>();   // tip, vrednost npr: grupa, 301a. da bi se znalo sta taj podatak predstavlja

    public Term(Room room, Time time) {
        this.room = room;
        this.time = time;
    }
}
