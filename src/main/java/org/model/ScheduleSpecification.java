package org.model;

import lombok.Getter;
import lombok.Setter;
import org.exceptions.DifferentDateException;
import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public abstract class ScheduleSpecification {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> excludedDays = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    /*
    - inicijalizacija rasporeda
    - dodavanje prostorija sa osobinama (kapacitet, računari, projektor)
    - dodavanje novog termina uz provere o zauzetost, obraditi situaciju da je termin već zauzet
    - brisanje zauzetog termina
    - premeštanje termina - brisanje i dodavanje novog termina sa istim vezanim podacima
    */

    /**
     * initialize schedule
     * @param startDate
     * @param endDate
     * @param excludedDays
     * @throws DifferentDateException
     */
    public abstract void initialize(LocalDate startDate, LocalDate endDate, List<LocalDate> excludedDays) throws DifferentDateException;  //mogu pocetne vrednosti
    public void addRoom(String name, int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException {
        Room room = new Room(name, capacity, equipment);
        if(rooms.contains(room))
            throw new RoomAlreadyExistsException();
        else
            rooms.add(room);
    }

    // bez opreme
    public void addRoom(String name, int capacity) throws RoomAlreadyExistsException {
        addRoom(name, capacity, null);
    }
    // TODO: brisanje prostorija ili izmena?
    public abstract void addTerm(Term term,String weekDay) throws TermAlreadyExistsException;


    // TODO: termAvailable ce se koristiti za dodavanje, brisanje i izmenu
    public abstract boolean termAvailable(Term term, String weekDay);

    //TODO: pri proveri zauzetosti uzeti u obzir excludedDays
    public void deleteTerm(Term term, String weekDay) throws TermDoesNotExistException {
        if(!terms.contains(term))
            throw new TermDoesNotExistException();
        else
            terms.remove(term);
    }

    // mozda changeTerm ovde da se implementira
    public abstract void changeTerm(Term oldTerm, LittleTerm newTerm, String weekDay) throws TermDoesNotExistException, TermAlreadyExistsException;
    // moze da se napravi nova klasa kao littleTerm koja ce da sadrzi samo vreme i prostoriju
    //TODO: izlistavanje slobodnih termina, prostorija...
    public abstract void save(String filepath, String fileName);
    public abstract void load(String filename);
    public List<Term> getTerms() {
        return terms;
    }
}
