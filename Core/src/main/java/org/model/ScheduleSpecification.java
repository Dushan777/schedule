package org.model;

import lombok.Getter;
import lombok.Setter;
import org.exceptions.DifferentDateException;
import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    /**
     * initialize schedule
     * sets beginningDate, endingDate and excludedDays
     * adds rooms
     * @param filePath path of a file from which schedule is initialized
     * @throws IOException input output exception
     */
    public void initialize(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        /*
        DatumVazenja:10/02/2023,01/13/2024
        NeradniDani:11/15/2023,12/31/2023,01/01/2024,01/07/2024
        Ucionice:
        RAF1,30,Racunar-10,Tabla-1
        RAF2,20,Racunar-15,Projektor-1
        RAF3,15
        RAF4,24,Projektor-2
        RAF5,10,Tabla-1
         */
        while ((line = reader.readLine()) != null) {
            String dateFormat = "MM/dd/yyyy";
            if(line.startsWith("FormatDatuma:"))
                dateFormat = line.substring(13);
            else if (line.startsWith("DatumVazenja:")) {
                String[] dates = line.substring(13).split(",");
                beginningDate = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(dateFormat));
                endingDate = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(dateFormat));
            }
            else if (line.startsWith("NeradniDani:")) {
                String[] dates = line.substring(12).split(",");
                for (String date : dates) {
                    excludedDays.add(LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat)));
                }
            }
            else if (line.startsWith("Prostorije:")) {
                while ((line = reader.readLine()) != null) {
                    if(line.equals(""))
                        continue;
                    String[] roomData = line.split(",");
                    String name = roomData[0];
                    int capacity = Integer.parseInt(roomData[1]);
                    Map<String, Integer> equipment = null;
                    if (roomData.length > 2) {
                        equipment = new HashMap<>();
                        for (int i = 2; i < roomData.length; i++) {
                            String[] equipmentData = roomData[i].split("-");
                            equipment.put(equipmentData[0], Integer.parseInt(equipmentData[1]));
                        }
                    }
                    try {
                        addRoom(name, capacity, equipment);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }

        }
        rooms.forEach(System.out::println);
    }


    /**
     * add room to schedule with equipment
     * @param name name of the room
     * @param capacity capacity of the room
     * @param equipment equipment of the room
     * @throws RoomAlreadyExistsException room already exists
     * @throws IllegalArgumentException illegal arguments
     */
    public void addRoom(String name, int capacity, Map<String, Integer> equipment) throws RoomAlreadyExistsException, IllegalArgumentException {
        Room room = new Room(name, capacity, equipment);
        if(rooms.contains(room))
            throw new RoomAlreadyExistsException();
        else
        {
            if(equipment != null && !equipment.isEmpty())
            {
                for(String key : equipment.keySet())
                {
                    if(equipment.get(key) <= 0)
                        throw new IllegalArgumentException("Equipment quantity must be positive");
                }
            }
            rooms.add(room);

        }
    }

    /**
     * add room to schedule without equipment
     * @param name name of the room
     * @param capacity  capacity of the room
     * @throws RoomAlreadyExistsException room already exists
     */
    public void addRoom(String name, int capacity) throws RoomAlreadyExistsException {
        addRoom(name, capacity, null);
    }

    /**
     * add term to schedule
     * @param term term you want to add
     * @param weekDay day of the week
     * @throws TermAlreadyExistsException term you want to add already exists
     * @throws DifferentDateException invalid dates
     * @throws IllegalArgumentException illegal arguments
     */
    public abstract void addTerm(Term term,String weekDay) throws TermAlreadyExistsException,DifferentDateException,IllegalArgumentException;



    /**
     * check if term is available
     * @param term term you want to check
     * @param weekDay day of the week
     * @return true if term is available, false otherwise
     */

    public abstract boolean termAvailable(Term term, String weekDay);

    /**
     * check if term is booked
     * @param term term you want to check
     * @param weekDay day of the  week
     * @return true if term is booked, false otherwise
     */
    // NE MORA DA SE TESTIRA
    public boolean termBooked(Term term, String weekDay) {
        return !termAvailable(term, weekDay);
    }


    /**
     * delete term from schedule
     * @param term term you want to delete
     * @param weekDay day of the week
     * @throws TermDoesNotExistException unexisting term
     * @throws IllegalArgumentException illegal arguments
     */
    public abstract void deleteTerm(Term term, String weekDay) throws TermDoesNotExistException, IllegalArgumentException;


    /**
     * change term with keeping additional data
     *
     * @param oldTerm term for change
     * @param newTerm term with new data
     * @param weekDay day of the week
     * @throws TermDoesNotExistException unexisting term
     * @throws TermAlreadyExistsException term already exists
     * @throws DifferentDateException startDate and endDate are different
     */
    public abstract void changeTerm(Term oldTerm, LittleTerm newTerm, String weekDay) throws TermDoesNotExistException, TermAlreadyExistsException,DifferentDateException,IllegalArgumentException;




    /**
     *
     * @return all free terms
     */

    public List<Term> allFreeTerms()
    {
        List<Term> freeTerms = new ArrayList<>();
        List<Term> sortedTerms = new ArrayList<>(terms);
        sortedTerms.sort(new TermComparator());
        LocalDate date = beginningDate;
        LocalDate date1;
        for(Term t : sortedTerms)
        {
            // Raf 1 10-12 1.1.2020 1.1.2020
            // Raf 1 12-13 1.1.2020 1.1.2020
            // Raf 1 15-16 1.1.2020 1.1.2020
            // Raf 1 10-12 5.1.2020 5.1.2020
            // Raf 1 13-14 5.1.2020 5.1.2020
            // Raf 2 10-12 1.1.2020 1.1.2020
            // Raf 2 12-13 1.1.2020 1.1.2020


            // za index 0
            if(sortedTerms.indexOf(t) == 0)
            {
                LocalDate currentDate = beginningDate;
                while(!currentDate.isEqual(t.getTime().getStartDate()))
                {
                    freeTerms.add(new Term(t.getRoom(), new Time(currentDate, currentDate, LocalTime.of(0,0), LocalTime.of(23,59)), null));
                    currentDate = currentDate.plusDays(1);
                }
                if(!t.getTime().getStartTime().equals(LocalTime.of(0,0)))
                    freeTerms.add(new Term(t.getRoom(), new Time(currentDate, currentDate, LocalTime.of(0,0), t.getTime().getStartTime()), null));

                continue;
            }

            date = sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getStartDate();
            date1 = t.getTime().getStartDate();

            // ako su iste sobe
            if(t.getRoom().getName().equals(sortedTerms.get(sortedTerms.indexOf(t) - 1).getRoom().getName())) {

                // ako su datumi razliciti (to znaci da je to poslednji termin u danu) onda dodajemo slobodne termine, prvo popunjavamo ceo zapocet dan
                // tako sto stavimo vreme pocetka na vreme kraja termina koji se zavrsio
                // a onda sve ostale dane do kraja
                if (!date.isEqual(date1)) {

                    // u slucaju da se prethodni termin ne zavrsava u 23:59, dodajemo slobodan termin od kraja termina do kraja dana
                    if(!sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime().equals(LocalTime.of(23,59)))
                        freeTerms.add(new Term(t.getRoom(), new Time(date, date, sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime(), LocalTime.of(23,59)), null));
                    date = date.plusDays(1);
                    // ako udje ovde to znaci da ima bar jedan ceo dan da je skroz slobodan
                    while (!date.isEqual(date1)) {

                        freeTerms.add(new Term(t.getRoom(), new Time(date, date, LocalTime.of(0,0), LocalTime.of(23,59)), null));
                        date = date.plusDays(1);
                    }
                    // ovde uzimamo od pocetka dana do pocetka termina
                    // ovde se ne uzima index t - 1 jer nam treba pocetak trenutnog termina, a gore nam je trebao kraj prethodnog
                    if(!sortedTerms.get(sortedTerms.indexOf(t)).getTime().getStartTime().equals(LocalTime.of(0,0)))
                        freeTerms.add(new Term(t.getRoom(), new Time(date1, date1, LocalTime.of(0,0), t.getTime().getStartTime()), null));
                }
                else
                {   // dodavace kao slobodan termin novi termin koji pocinje kad se prethodni zavrsi, a za kraj uzima pocetak trenutnog termina
                    if(!sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime().equals(t.getTime().getStartTime()))
                        freeTerms.add(new Term(t.getRoom(), new Time(date, date, sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime(), t.getTime().getStartTime()), null));
                }
            }

            // ako su razlicita imena treba da dodamo za prethodnu prostoriju termine do kraja rasporeda i do kraja dana
            // i onda da dodamo za trenutnu prostoriju termine od pocetka rasporeda do pocetka trenutnog termina
            else
            {
                if(!sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime().equals(LocalTime.of(23,59)))
                    freeTerms.add(new Term(sortedTerms.get(sortedTerms.indexOf(t) - 1).getRoom(), new Time(date, date, sortedTerms.get(sortedTerms.indexOf(t) - 1).getTime().getEndTime(), LocalTime.of(23,59)), null));
                date = date.plusDays(1);
                while(!date.isAfter(endingDate))
                {
                    freeTerms.add(new Term(sortedTerms.get(sortedTerms.indexOf(t) - 1).getRoom(), new Time(date, date, LocalTime.of(0,0), LocalTime.of(23,59)), null));
                    date = date.plusDays(1);
                }
                // dodajemo slobodne termine od pocetka rasporeda do pocetka termina
                LocalDate currentDate = beginningDate;
                while(!currentDate.isEqual(date1))
                {
                    freeTerms.add(new Term(t.getRoom(), new Time(currentDate, currentDate, LocalTime.of(0,0), LocalTime.of(23,59)), null));
                    currentDate = currentDate.plusDays(1);
                }
                if(!t.getTime().getStartTime().equals(LocalTime.of(0,0)))
                    freeTerms.add(new Term(t.getRoom(), new Time(date1, date1, LocalTime.of(0,0), t.getTime().getStartTime()), null));

            }
        }
        // ovo je poslednji termin u rasporedu
        if(terms.size() > 0)
        {
            Term lastTerm = sortedTerms.get(sortedTerms.size() - 1);
            if(!lastTerm.getTime().getEndTime().equals(LocalTime.of(23,59)))
                freeTerms.add(new Term(lastTerm.getRoom(), new Time(lastTerm.getTime().getStartDate(), lastTerm.getTime().getStartDate(), lastTerm.getTime().getEndTime(), LocalTime.of(23,59)), null));
            date = lastTerm.getTime().getStartDate().plusDays(1);
            while(!date.isAfter(endingDate))
            {
                freeTerms.add(new Term(lastTerm.getRoom(), new Time(date, date, LocalTime.of(0,0), LocalTime.of(23,59)), null));
                date = date.plusDays(1);
            }
        }
        // ako su neke sobe skroz prazne stavimo im svaki dan kao slobodan
        for(Room r : rooms) {
            boolean found = false;
            for (Term t : freeTerms) {
                if (t.getRoom().getName().equals(r.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LocalDate curr = beginningDate;
                while (!curr.isAfter(endingDate)) {
                    freeTerms.add(new Term(r, new Time(curr, curr, LocalTime.of(0, 0), LocalTime.of(23, 59)), null));
                    curr = curr.plusDays(1);
                }
            }
        }
        List<Term> finalTerms = new ArrayList<>(freeTerms);
        for(Term t : freeTerms)
        {
            if(getExcludedDays() == null)
                break;
            if(getExcludedDays().contains(t.getTime().getStartDate()))
                finalTerms.remove(t);
        }
        finalTerms.sort(new TermComparator());
        return finalTerms;
    }


    private List<Term> filterByRooms(String name,int capacity, Map<String,Integer> equipment, List<Term> termss, boolean booked)
    {

        List<Term> filteredTerms;
        if(booked)
            filteredTerms = new ArrayList<>(termss);
        else
            filteredTerms = new ArrayList<>(allFreeTerms());

        List<Term> filteredTerms2 = new ArrayList<>(filteredTerms);
        for(Term t : filteredTerms2)
        {
            if(capacity > 0 && t.getRoom().getCapacity() < capacity)
                    filteredTerms.remove(t);
            if(name != null && !name.equals("") && !t.getRoom().getName().equals(name))
                    filteredTerms.remove(t);
            if(equipment != null && !equipment.isEmpty() && !mapHasEquipment(t.getRoom().getEquipment(),equipment))
                    filteredTerms.remove(t);
        }
        return filteredTerms;
    }

    /**
     * Filter by rooms
     * @param name if "" or null, it is not used in filtering
     * @param capacity if less than or equal to 0, it is not used in filtering
     * @param equipment if empty or null, it is not used in filtering
     * @param booked if true, filter booked terms, if false, filter free terms
     * @return filtered terms
     */
    public List<Term> filterByRooms(String name,int capacity, Map<String,Integer> equipment, boolean booked)
    {
        return filterByRooms(name,capacity,equipment,terms,booked);
    }


    private List<Term> filterByTimeOrAdditionalData(Time time, Map<String,String> additionalData, String weekDay, List<Term> termss, boolean booked)
    {
        List<Term> filteredTerms;
        if(booked)
             filteredTerms = new ArrayList<>(termss);
        else
        {
            if(terms.equals(termss))
                filteredTerms = new ArrayList<>(allFreeTerms());
            else
                filteredTerms = new ArrayList<>(termss);
        }
        List<Term> temp = new ArrayList<>(filteredTerms);
        for(Term t : temp)
        {
            if(time.getStartDate()!=null && time.getStartDate().isAfter(t.getTime().getStartDate()))
                filteredTerms.remove(t);
            // ako se prosledjeni zavrsava pre kraja termina
            if(time.getEndDate()!=null && time.getEndDate().isBefore(t.getTime().getEndDate()))
                filteredTerms.remove(t);
            if(weekDay != null && !weekDay.equals("") && !Time.getWeekDay(t.getTime().getStartDate()).equals(weekDay))
                filteredTerms.remove(t);

            if(booked) {
                if (additionalData != null && !additionalData.isEmpty() && !mapHasAdditionalData(t.getAdditionalData(), additionalData))
                    filteredTerms.remove(t);

                // ako prosledjeni pocinje nakon pocetka termina

                if (time.getStartTime() != null && time.getStartTime().isAfter(t.getTime().getStartTime()))
                    filteredTerms.remove(t);

                if (time.getEndTime() != null && time.getEndTime().isBefore(t.getTime().getEndTime()))
                    filteredTerms.remove(t);
            }
            else
            {
                /*
                11-12
                11-null time
                null-12
                00-08 t
                18-19 t
                Raf 1 10-12 1.1.2020 1.1.2020
                Raf 1 00-23:59 2.1.2020 2.1.2020
                 */

                                                    // prosledjen pocetak je pre pocetka termina            // prosledjen pocetak je posle kraja termina
                // logika : ako mi treba da zakazem cas koji ce poceti u 14h najkasnije
                // 14 - null
                if (time.getEndTime() == null && time.getStartTime() != null && (time.getStartTime().isBefore(t.getTime().getStartTime()) || time.getStartTime().isAfter(t.getTime().getEndTime())))
                    filteredTerms.remove(t);
                                                    // prosledjen kraj je posle kraja termina               // prosledjen kraj je pre pocetka termina
                // logika : ako mi treba da zakazem cas koji ce se zavrsiti u 14h najranije
                // null - 14
                if (time.getStartTime() == null && time.getEndTime() != null && (time.getEndTime().isAfter(t.getTime().getEndTime()) || time.getEndTime().isBefore(t.getTime().getStartTime())))
                    filteredTerms.remove(t);
                // logika : vracam termine koji su slobodni u intervalu koji obuhvata 14-16
                // 14 - 16
                if(time.getStartTime() != null && time.getEndTime() != null && (time.getStartTime().isBefore(t.getTime().getStartTime()) || time.getEndTime().isAfter(t.getTime().getEndTime())))
                    filteredTerms.remove(t);
            }
        }
        return filteredTerms;
    }

    /**
     * Filter by time or additional data
     * @param time if any part of time is null, it is not used in filtering
     * @param additionalData if empty or null, it is not used in filtering
     * @param weekDay if "" it is not used in filtering
     * @param booked if true, filter booked terms, if false, filter free terms
     * @return filtered terms
     */
    public List<Term> filterByTimeOrAdditionalData(Time time, Map<String,String> additionalData, String weekDay, boolean booked)
    {
        return filterByTimeOrAdditionalData(time,additionalData,weekDay,terms,booked);
    }


    private List<Term> filterByEverything(String name,int capacity, Map<String,Integer> equipment,Time time, Map<String,String> additionalData, String weekDay, List<Term> termss, boolean booked)
    {
        List<Term> filteredTerms;
        List<Term> filteredTerms2;

        filteredTerms = filterByRooms(name,capacity,equipment,booked);
        filteredTerms2 = filterByTimeOrAdditionalData(time,additionalData,weekDay,filteredTerms, booked);
        return filteredTerms2;
    }

    /**
     * Filter by everything
     * Combination of filterByRooms and filterByTimeOrAdditionalData
     * @param name room name, if "" or null, it is not used in filtering
     * @param capacity room capacity, if less than or equal to 0, it is not used in filtering
     * @param equipment room equipment, if empty or null, it is not used in filtering
     * @param time if any part of time is null, it is not used in filtering
     * @param additionalData if empty or null, it is not used in filtering
     * @param weekDay if "" it is not used in filtering
     * @param booked if true, filter booked terms, if false, filter free terms
     * @return filtered terms
     */
    public List<Term> filterByEverything(String name,int capacity, Map<String,Integer> equipment,Time time, Map<String,String> additionalData, String weekDay, boolean booked)
    {
        return filterByEverything(name,capacity,equipment,time,additionalData,weekDay,terms,booked);
    }

    /**
     * save schedule to file as JSON
     * @param terms terms you want to save
     * @param fileName name of file you want to save to
     * @throws IOException input output exception
     */
    public abstract void saveAsJSON(List<Term> terms, String fileName) throws IOException;

    /**
     * save schedule to file as JSON
     * @param terms terms you want to save
     * @param filePath name of file you want to save to
     * @throws IOException input output exception
     */
    public abstract void saveAsCSV(List<Term> terms,String filePath) throws IOException;

    /**
     * save schedule to file as PDF
     * @param terms terms you want to save
     * @param filePath name of file you want to save to
     * @throws IOException input output exception
     */
    public abstract void saveAsPDF(List<Term> terms,String filePath) throws IOException;

    /**
     * load schedule from JSON file
     * @param fileName name of the file
     * @param configPath name of config file
     * @throws IOException input output exception
     * @throws TermAlreadyExistsException term already exists
     * @throws DifferentDateException startDate and endDate are different
     */
    public abstract void loadFromJSON(String fileName, String configPath) throws IOException, TermAlreadyExistsException, DifferentDateException;

    /**
     * load schedule from CSV file
     * @param fileName name of file you want to load from
     * @param configPath name of config file
     * @throws IOException input output exception
     * @throws TermAlreadyExistsException term already exists
     * @throws DifferentDateException startDate and endDate are different
     */
    public abstract void loadFromCSV(String fileName, String configPath) throws IOException, DifferentDateException, TermAlreadyExistsException;

    private boolean mapHasEquipment(Map<String,Integer> e,Map<String,Integer> e2)
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
    protected boolean termsOverlap(Term t, Term term) {
        if(!t.getTime().getStartDate().equals(term.getTime().getStartDate()))
            return false;
        if (t.getRoom().equals(term.getRoom()))
            return !((t.getTime().getEndTime().isBefore(term.getTime().getStartTime()) || t.getTime().getStartTime().isAfter(term.getTime().getEndTime())
                    || t.getTime().getEndTime().equals(term.getTime().getStartTime()) || t.getTime().getStartTime().equals(term.getTime().getEndTime())));
        return false;
    }
    protected static List<ConfigMapping> readConfig(String filePath) throws FileNotFoundException {
        List<ConfigMapping> mappings = new ArrayList<>();

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" ", 3);

            mappings.add(new ConfigMapping(Integer.valueOf(splitLine[0]), splitLine[1], splitLine[2]));
        }

        scanner.close();


        return mappings;
    }

}
