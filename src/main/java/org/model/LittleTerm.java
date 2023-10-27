package org.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LittleTerm {

    private Room room;
    private Time time;

    public LittleTerm(Room room, Time time) {
        this.room = room;
        this.time = time;
    }
}
