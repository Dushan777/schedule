package org.model;

import lombok.Getter;
import lombok.Setter;
import org.exceptions.DifferentDateException;
import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;

import java.time.LocalDate;
import java.util.ArrayList;
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

    /**
     * add room to schedule with equipment
     * @param name
     * @param capacity
     * @param equipment
     * @throws RoomAlreadyExistsException
     */
    public void addRoom(String name, int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException {
        Room room = new Room(name, capacity, equipment);
        if(rooms.contains(room))
            throw new RoomAlreadyExistsException();
        else
            rooms.add(room);
    }

    /**
     * add room to schedule without equipment
     * @param name
     * @param capacity
     * @throws RoomAlreadyExistsException
     */
    public void addRoom(String name, int capacity) throws RoomAlreadyExistsException {
        addRoom(name, capacity, null);
    }
    // TODO: brisanje prostorija ili izmena?

    /**
     * add term to schedule
     * @param term
     * @param weekDay
     * @throws TermAlreadyExistsException
     */
    public abstract void addTerm(Term term,String weekDay) throws TermAlreadyExistsException;


    // TODO: termAvailable ce se koristiti za dodavanje i izmenu

    /**
     * check if term is available
     * @param term
     * @param weekDay
     * @return
     */

    public abstract boolean termAvailable(Term term, String weekDay);

    //TODO: pri proveri zauzetosti uzeti u obzir excludedDays

    /**
     * delete term from schedule
     * @param term
     * @param weekDay
     * @throws TermDoesNotExistException
     */
    public abstract void deleteTerm(Term term, String weekDay) throws TermDoesNotExistException;


    /**
     * change term with keeping additional data
     * @param oldTerm
     * @param newTerm
     * @param weekDay
     * @throws TermDoesNotExistException
     * @throws TermAlreadyExistsException
     */
    //TODO: mozda changeTerm ovde da se implementira
    public abstract void changeTerm(Term oldTerm, LittleTerm newTerm, String weekDay) throws TermDoesNotExistException, TermAlreadyExistsException;

    //TODO: izlistavanje slobodnih termina, prostorija...

    /**
     * save schedule to file
     * @param filepath
     * @param fileName
     */
    public abstract void save(String filepath, String fileName);

    /**
     * load schedule from file
     * @param filename
     */
    public abstract void load(String filename);
    public List<Term> getTerms() {
        return terms;
    }
}
