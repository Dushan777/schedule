import org.example.ScheduleCollection;
import org.exceptions.DifferentDateException;
import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.model.LittleTerm;
import org.model.Room;
import org.model.Term;
import org.model.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ScheduleCollectionTest {
    @Test
    public void termAvailableTest() throws RoomAlreadyExistsException {

        // moze da se desi da nece da radi zbog now
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        String dateStr = "2023-06-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        System.out.println("da");
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.getExcludedDays().add(localDate);
        scheduleCollection.addRoom("Raf1",30,null);
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(2));
        scheduleCollection.setEndingDate(LocalDate.now().minusWeeks(1));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(1),sad),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(10)),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),sad),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),sad.minusHours(4)),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),sad.minusHours(2)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),sad.minusHours(2)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),sad.minusHours(3)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(5),sad.minusHours(3)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(1), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(localDate,localDate, sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));

        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(localDate,localDate.minusDays(1), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(localDate,localDate.plusDays(1), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));


        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(1), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(10),LocalDate.now().minusWeeks(10), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now(),LocalDate.now(), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertTrue(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusDays(10),LocalDate.now().minusDays(10), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusDays(15),LocalDate.now().minusDays(15), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusDays(6),LocalDate.now().minusDays(6), sad.minusHours(6),sad.minusHours(1)),null),"Saturday"));

        assertFalse(scheduleCollection.termAvailable(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));
        assertFalse(scheduleCollection.termAvailable(new Term(new Room("Raf1",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(12),sad.minusHours(11)),null),"Saturday"));

    }
    @Test
    public void addTermTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        String dateStr = "2023-09-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        scheduleCollection.getExcludedDays().add(localDate);
        scheduleCollection.addRoom("Raf1",30,null);
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(2));
        scheduleCollection.setEndingDate(LocalDate.now().minusWeeks(1));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null));
        assertThrows(TermAlreadyExistsException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(DifferentDateException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf8",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf9",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));

    }

    @Test
    public void deleteTermTest() throws TermDoesNotExistException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null));
        assertThrows(TermDoesNotExistException.class,()->scheduleCollection.deleteTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(TermDoesNotExistException.class,()->scheduleCollection.deleteTerm(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleCollection.addTerm(new Term(new Room("Raf1",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));

        // assertDoesNotThrow
        scheduleCollection.deleteTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday");
        for(Term t : scheduleCollection.getTerms())
        {
            System.out.println(t);
        }
    }

    // za test: jedan normalan, jedan gde nema sta da obrise, jedan gde je novi termin zauzet
    @Test
    public void changeTermTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(6));
        scheduleCollection.setEndingDate(LocalDate.now().plusWeeks(6));
        scheduleCollection.addRoom("Raf1",30,null);
        scheduleCollection.addRoom("Raf2",30,null);
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        assertThrows(TermDoesNotExistException.class, () -> scheduleCollection.changeTerm(new Term(new Room("Raf3",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
        , new LittleTerm(new Room("Raf3",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120)),"Saturday"));
        assertThrows(TermAlreadyExistsException.class, () -> scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));

        assertThrows(IllegalArgumentException.class, () -> scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, () -> scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(3),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf8",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf8",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));
        assertThrows(IllegalArgumentException.class, ()->scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf8",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday"));

        /*HashMap<String, String> h = new HashMap<>();
        h.put("prof", "uros");
        h.put("subject", "matematika");

        scheduleCollection.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),h)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),"Saturday");
        */
        for(Term t : scheduleCollection.getTerms())
        {
            System.out.println(t);
        }
    }
    @Test
    public void filterByEverythingBookedTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(6));
        scheduleCollection.setEndingDate(LocalDate.now().plusWeeks(6));

        HashMap<String, String> h = new HashMap<>();
        h.put("prof", "uros");
        h.put("subject", "matematika");

        HashMap<String, Integer> p = new HashMap<>();
        p.put("projektor", 2);
        p.put("subject", 1);

        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,p),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),60),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,p),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),h));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(4),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(2),120),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,p),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),h));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(5),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,p),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.plusHours(2),sad.plusHours(4)),null));

        /*for(Term term : scheduleCollection.getTerms())
            System.out.println(term);*/

        HashMap<String, String> l = new HashMap<>();
        l.put("prof", "uros");
        HashMap<String, Integer> k = new HashMap<>();
        k.put("projektor", 1);
        scheduleCollection.addRoom("Raf3",30);
        List<Term> filtered;
        filtered = scheduleCollection.filterByEverything("", 21,null,new Time(LocalDate.now().minusWeeks(2),null,sad.minusHours(3),sad.minusHours(3)),null,null, false);
        for(Term term : filtered)
            System.out.println(term);
    }

    @Test
    public void allFreeTermsTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(3));
        scheduleCollection.setEndingDate(LocalDate.now());
        //scheduleCollection.getExcludedDays().add(LocalDate.now().minusWeeks(2).minusDays(2));
        HashMap<String, String> h = new HashMap<>();
        h.put("prof", "uros");
        h.put("subject", "matematika");

        HashMap<String, Integer> p = new HashMap<>();
        p.put("projektor", 2);
        p.put("subject", 1);

        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),60),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(4),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(2),120),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(5),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.plusHours(2),sad.plusHours(4)),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.plusHours(2),sad.plusHours(4)),null));

        /*for(Term term : scheduleCollection.getTerms())
            System.out.println(term);*/

        HashMap<String, String> l = new HashMap<>();
        l.put("prof", "uros");
        HashMap<String, Integer> k = new HashMap<>();
        k.put("projektor", 1);

        List<Term> filtered;
        scheduleCollection.addRoom("Raf3",30);
        filtered = scheduleCollection.allFreeTerms();
        for(Term term : filtered)
            System.out.println(term);
    }
    @Test
    public void filterByRoomTest()
    {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(3));
        scheduleCollection.setEndingDate(LocalDate.now());

        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),60),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(4),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(2),120),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(5),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.plusHours(2),sad.plusHours(4)),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.plusHours(2),sad.plusHours(4)),null));

        List<Term> filter = scheduleCollection.filterByRooms("",21,null,false);
        for(Term term : filter)
            System.out.println(term);
    }

    @Test
    public void filterByTimeOrAdditionalDataTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        ScheduleCollection scheduleCollection = new ScheduleCollection();
        scheduleCollection.setBeginningDate(LocalDate.now().minusWeeks(3));
        scheduleCollection.setEndingDate(LocalDate.now());

        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),60),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(6),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(4),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2).minusDays(1),LocalDate.now().minusWeeks(2).minusDays(1), sad.minusHours(2),120),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(2),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(5),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf2",20,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.plusHours(2),sad.plusHours(4)),null));

        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.minusHours(9),120),null));
        scheduleCollection.getTerms().add(new Term(new Room("Raf4",20,null),new Time(LocalDate.now().minusWeeks(2).plusDays(5),LocalDate.now().minusWeeks(2).plusDays(5), sad.plusHours(2),sad.plusHours(4)),null));

        scheduleCollection.addRoom("Raf3",30);

        List<Term> filtered;
        filtered = scheduleCollection.filterByTimeOrAdditionalData(new Time(LocalDate.now().minusWeeks(2).minusDays(3),null, sad.minusHours(3),sad.plusHours(1)),null,null,false);
        for(Term term : filtered)
            System.out.println(term);
    }




}
