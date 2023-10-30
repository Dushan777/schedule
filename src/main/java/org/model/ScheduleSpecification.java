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

    private LocalDate beginningDate;
    private LocalDate endingDate;
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

    // TODO: uskladiti exceptione izmedju specifikacija i implementacija
    /**
     * initialize schedule
     * MUST be called before any other method
     * MUST have startDate and endDate
     * @param startDate
     * @param endDate
     * @param excludedDays
     */
    public abstract void initialize(LocalDate startDate, LocalDate endDate, List<LocalDate> excludedDays);
    /*{
        setExcludedDays(excludedDays);
        setBeginningDate(startDate);
        setEndingDate(endDate);
    }
    */

    /**
     * add room to schedule with equipment
     * @param name
     * @param capacity
     * @param equipment
     * @throws RoomAlreadyExistsException
     * @throws IllegalArgumentException
     */
    public void addRoom(String name, int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException, IllegalArgumentException {
        Room room = new Room(name, capacity, equipment);
        if(rooms.contains(room))
            throw new RoomAlreadyExistsException();
        else
        {
            for(String key : equipment.keySet())
            {
                if(equipment.get(key) <= 0)
                    throw new IllegalArgumentException("Equipment quantity must be positive");
            }
            rooms.add(room);
        }
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

    /**
     * add term to schedule
     * @param term
     * @param weekDay
     * @throws TermAlreadyExistsException
     * @throws DifferentDateException
     */
    public abstract void addTerm(Term term,String weekDay) throws TermAlreadyExistsException,DifferentDateException;



    /**
     * check if term is available
     * @param term
     * @param weekDay
     * @return true if term is available, false otherwise
     */

    public abstract boolean termAvailable(Term term, String weekDay);

    /**
     * check if term is booked
     * @param term
     * @param weekDay
     * @return true if term is booked, false otherwise
     */
    // NE MORA DA SE TESTIRA
    public boolean termBooked(Term term, String weekDay) {
        return !termAvailable(term, weekDay);
    }


    /**
     * delete term from schedule
     * @param term
     * @param weekDay
     * @throws TermDoesNotExistException
     */
    public abstract void deleteTerm(Term term, String weekDay) throws TermDoesNotExistException;


    /**
     * change term with keeping additional data
     * When using this method in ScheduleCollection, weekday is not used
     * When using this method in ScheduleWeekly, weekday is used and has to be the same in oldTerm and newTerm
     *
     * @param oldTerm
     * @param newTerm
     * @param weekDay
     * @throws TermDoesNotExistException
     * @throws TermAlreadyExistsException
     * @throws DifferentDateException
     */
    //TODO: mozda changeTerm ovde da se implementira
    public abstract void changeTerm(Term oldTerm, LittleTerm newTerm, String weekDay) throws TermDoesNotExistException, TermAlreadyExistsException,DifferentDateException;

    //TODO: izlistavanje slobodnih termina, prostorija...

    /*
    Najbitnije operacije nad rasporedom su provera zauzetosti termina i prostora i izlistavanje slobodnih
    termina po različitim kriterijumima. Vreme se prilikom ovih provera može zadavati na dva načina, prvi je
    zadavanje tačnog datuma, a drugi je zadavanje dana u nedelji i perioda (na primer da li je slobodan
    termin sredom 10-12h u periodu od 1.10.2023. do 1.12.2023). Termini se mogu zadavati kao vreme
    početka i završetka ili kao vreme početka i trajanje. Izlistavanje slobodnih termina može da uključi i
    tačnu prostoriju, prostoriju sa određenim osobinama (na primer učionica sa računarima, projektorom,

    da ima više od 30 mesta i slično), a može i da bude nezavisno od prostorije (izlistati sve učionice koja je
    slobodna tog i tog dana). Obezbediti i pretraživanje rasporeda prema vezanim podacima (na primer ako
    je nastavnik vezani podatak, izlistati sve termine tog nastavnika ili sve termine kada je on slobodan).
    Potrebno je obezbediti operacije za izlistavanje slobodnih termina, ali i zauzetih termina, provera da li je
    određeni termin slobodan ili zauzet, po raličitim kriterijumima kako je prethodno objašnjeno.
     */

    /**
     * Filter by rooms
     * if name is "" or capacity <= 0 or equipment is empty, it is not used in filtering
     * termss must not be null
     * @param name
     * @param capacity
     * @param equipment
     * @param termss
     * @return filtered terms
     */
    public List<Term> filterByRooms(String name,int capacity, Map<String,Integer> equipment, List<Term> termss)
    {
        List<Term> filteredTerms = new ArrayList<>(termss);
        for(Term t : termss)
        {
            if(capacity > 0 && t.getRoom().getCapacity() < capacity)
                    filteredTerms.remove(t);
            if(!name.equals("") && !t.getRoom().getName().equals(name))
                    filteredTerms.remove(t);
            if(!equipment.isEmpty() && !mapHasEquipment(t.getRoom().getEquipment(),equipment))
                    filteredTerms.remove(t);
        }
        return filteredTerms;
    }

    // kod opcije za samo jedan dan necemo imati endDate i weekDay provere, ali mislim da ne treba jos jedna metoda
    // jer je ovo dovoljno opsta, ali ovo je u slucaju da on ima obe opcije u obe implementacije sto je nama logicno

    /**
     * Filter by time or additional data
     * if any part of time is null, it is not used in filtering
     * if additionalData is empty, it is not used in filtering
     * if weekDay is "", it is not used in filtering
     * termss must not be null
     * time is interval from-to
     * @param time
     * @param additionalData
     * @param weekDay
     * @param termss
     * @return filtered terms
     */
    public List<Term> filterByTimeOrAdditionalData(Time time, Map<String,String> additionalData, String weekDay, List<Term> termss)
    {
        List<Term> filteredTerms = new ArrayList<>(termss);
        for(Term t : termss)
        {
            if(!additionalData.isEmpty() && !mapHasAdditionalData(t.getAdditionalData(),additionalData))
                filteredTerms.remove(t);

            // ako prosledjeni pocinje nakon pocetka termina
            if(time.getStartDate()!=null && time.getStartDate().isAfter(t.getTime().getStartDate()))
                filteredTerms.remove(t);
            // ako se prosledjeni zavrsava pre kraja termina
            if(time.getEndDate()!=null && time.getEndDate().isBefore(t.getTime().getEndDate()))
                filteredTerms.remove(t);

            if(time.getStartTime()!=null && time.getStartTime().isAfter(t.getTime().getStartTime()))
                filteredTerms.remove(t);

            if(time.getEndTime()!=null && time.getEndTime().isBefore(t.getTime().getEndTime()))
                filteredTerms.remove(t);

            if(!weekDay.equals("") && !Time.getWeekDay(t.getTime().getStartDate()).equals(weekDay))
                filteredTerms.remove(t);
        }
        return filteredTerms;
    }

    /**
     * Filter by everything
     * Combination of filterByRooms and filterByTimeOrAdditionalData
     * @param name
     * @param capacity
     * @param equipment
     * @param time
     * @param additionalData
     * @param weekDay
     * @param termss
     * @return filtered terms
     */
    public List<Term> filterByEverything(String name,int capacity, Map<String,Integer> equipment,Time time, Map<String,String> additionalData, String weekDay, List<Term> termss)
    {
        List<Term> filteredTerms;
        List<Term> filteredTerms2;

        filteredTerms = filterByRooms(name,capacity,equipment,termss);
        filteredTerms2 = filterByTimeOrAdditionalData(time,additionalData,weekDay,filteredTerms);
        return filteredTerms2;
    }

    /**
     * save schedule to file
     * can be PDF, CSV, JSON
     * @param filepath
     * @param fileName
     */
    public abstract void save(String filepath, String fileName);

    /**
     * load schedule from file
     * can be CSV, JSON
     * @param filename
     */
    public abstract void load(String filename);

    private boolean mapHasEquipment(Map<String,Integer> e,Map<String,Integer> e2 )
    {
        for(String key : e2.keySet())
        {
            if(!e.containsKey(key))
                return false;
            else if(e.get(key) < e2.get(key))
                return false;
        }
        return true;
    }

    private boolean mapHasAdditionalData(Map<String,String> e,Map<String,String> e2 )
    {
        for(String key : e2.keySet())
        {
            if(!e.containsKey(key))
                return false;
            else if(!e.get(key).equals(e2.get(key)))
                return false;
        }
        return true;
    }

}
