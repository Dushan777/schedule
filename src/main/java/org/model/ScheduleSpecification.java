package org.model;

import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ScheduleSpecification {

    private List<Term> terms = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    /*
    - inicijalizacija rasporeda
    - dodavanje prostorija sa osobinama (kapacitet, računari, projektor)
    - dodavanje novog termina uz provere o zauzetost, obraditi situaciju da je termin već zauzet
    - brisanje zauzetog termina
    - premeštanje termina - brisanje i dodavanje novog termina sa istim vezanim podacima
    */
    public abstract void initialize();
    public void addRoom(int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException {
        Room room = new Room("name", capacity, equipment);
        if(rooms.contains(room))
            throw new RoomAlreadyExistsException();
        else
            rooms.add(room);
    }

    // bez opreme
    public void addRoom(int capacity) throws RoomAlreadyExistsException {
        addRoom(capacity, null);
    }
    // TODO: brisanje prostorija ili izmena?
    public abstract void addTerm(Term term) throws TermAlreadyExistsException;
    // TODO: termAvailable ce se koristiti za dodavanje, brisanje i izmenu
    public abstract boolean termAvailable(Term term);
    public abstract void deleteTerm(Term term) throws TermDoesNotExistException;
    public abstract void changeTerm(Term oldTerm, Term newTerm) throws TermDoesNotExistException;

    //TODO: izlistavanje slobodnih termina, prostorija...
    public abstract void save(String filepath, String fileName);
    public abstract void load(String filename);
    public List<Term> getTerms() {
        return terms;
    }
}
