package org.model;

import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ScheduleSpecification {

    private List<Term> terms = new ArrayList<>();
    /*
    - inicijalizacija rasporeda
    - dodavanje prostorija sa osobinama (kapacitet, računari, projektor)
    - dodavanje novog termina uz provere o zauzetost, obraditi situaciju da je termin već zauzet
    - brisanje zauzetog termina
    - premeštanje termina - brisanje i dodavanje novog termina sa istim vezanim podacima
    */
    public abstract void initialize();
    public abstract void addRoom(int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException;
    // TODO: brisanje prostorija ili izmena?
    public abstract void addTerm(Term term) throws TermAlreadyExistsException;
    // TODO: termExists ce se koristiti za dodavanje, brisanje i izmenu
    public abstract boolean termAvailable(Term term);
    public abstract void deleteTerm(Term term) throws TermDoesNotExistException;
    public abstract void changeTerm(Term oldTerm, Term newTerm) throws TermDoesNotExistException;

    //TODO: izlistavanje termina, prostorija...
    public abstract void save(String file);
    public abstract void load(String file);
    public List<Term> getTerms() {
        return terms;
    }
}
