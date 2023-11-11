import org.exceptions.RoomAlreadyExistsException;
import org.exceptions.TermAlreadyExistsException;
import org.example.ScheduleWeekly;
import org.junit.jupiter.api.Test;
import org.model.LittleTerm;
import org.model.Room;
import org.model.Term;
import org.model.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleWeeklyTest {

    @Test
    public void termAvailableTest() throws RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        String dateStr = "2023-10-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.now().minusDays(2);;
        System.out.println("da");
        ScheduleWeekly scheduleWeekly = new ScheduleWeekly();
        scheduleWeekly.getExcludedDays().add(localDate);
        scheduleWeekly.addRoom("Raf1", 30, null);
        scheduleWeekly.setBeginningDate(LocalDate.now().minusWeeks(5));
        scheduleWeekly.setEndingDate(LocalDate.now().plusWeeks(4));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));

        //TODO: prepraviti da se ne koristi now
        assertTrue(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(1), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now(), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now())));
        assertTrue(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now(), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(3))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now())));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(6), LocalDate.now(), sad.minusHours(4), 120), null), "Friday"));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().plusWeeks(10), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertTrue(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        //assertFalse(scheduleWeekly.termAvailable(new Term(new Room("", 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room(null, 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 0, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", -1, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), "subota"));
        assertFalse(scheduleWeekly.termAvailable(new Term(new Room("Raf1", 30, null), new Time(null, LocalDate.now().plusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));



    }
    @Test
    public void addTermTest() throws TermAlreadyExistsException, RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        String dateStr = "2023-10-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.now().minusDays(2);
        System.out.println("da");
        ScheduleWeekly scheduleWeekly = new ScheduleWeekly();
        scheduleWeekly.getExcludedDays().add(localDate);
        scheduleWeekly.addRoom("Raf1", 30, null);
        scheduleWeekly.setBeginningDate(LocalDate.now().minusWeeks(5));
        scheduleWeekly.setEndingDate(LocalDate.now().plusWeeks(4));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));

        scheduleWeekly.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(5), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now()));
        scheduleWeekly.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(1), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1)));
        assertThrows(TermAlreadyExistsException.class ,() -> scheduleWeekly.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1))));
        scheduleWeekly.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(5), LocalDate.now().minusWeeks(4), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1)));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        //assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"subota"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));



        for(Term t : scheduleWeekly.getTerms())
            System.out.println(t);
        System.out.println(scheduleWeekly.getTerms().size());
    }

    @Test
    public void deleteTermTest()
    {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        String dateStr = "2023-10-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.now().minusDays(2);
        System.out.println("da");
        ScheduleWeekly scheduleWeekly = new ScheduleWeekly();
        scheduleWeekly.getExcludedDays().add(localDate);
        scheduleWeekly.setBeginningDate(LocalDate.now().minusWeeks(5));
        scheduleWeekly.setEndingDate(LocalDate.now().plusWeeks(4));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf2", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf1",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        //assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"subota"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",0,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));
        assertThrows(IllegalArgumentException.class,()->scheduleWeekly.addTerm(new Term(new Room("Raf4",-1,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null),"Saturday"));



        scheduleWeekly.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(3), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now()));
        scheduleWeekly.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now()));
        scheduleWeekly.deleteTerm(new Term(new Room("Raf2", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 119), null), Time.getWeekDay(LocalDate.now()));
        /*scheduleWeekly.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), "Saturday");
        scheduleWeekly.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), "Saturday");
        scheduleWeekly.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120),null), "Saturday");
    */
        for(Term t : scheduleWeekly.getTerms())
            System.out.println(t);
    }

    @Test
    public void changeTermTest() throws TermAlreadyExistsException, RoomAlreadyExistsException {
        LocalTime sad = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        String dateStr = "2023-10-28";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // ovo je samo da vidimo da l radi kad se stavi datum kao string
        LocalDate localDate = LocalDate.now().minusDays(2);
        System.out.println("da");
        ScheduleWeekly scheduleWeekly = new ScheduleWeekly();
        scheduleWeekly.getExcludedDays().add(localDate);
        scheduleWeekly.addRoom("Raf1", 30, null);
        scheduleWeekly.addRoom("Raf2", 30, null);
        scheduleWeekly.addRoom("Raf4", 30, null);
        scheduleWeekly.setBeginningDate(LocalDate.now().minusWeeks(5));
        scheduleWeekly.setEndingDate(LocalDate.now().plusWeeks(4));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf2", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));
        scheduleWeekly.getTerms().add(new Term(new Room("Raf4", 30, null), new Time(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null));


        scheduleWeekly.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now()));

        assertThrows(TermAlreadyExistsException.class,() -> scheduleWeekly.changeTerm(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));

        assertThrows(IllegalArgumentException.class,() -> scheduleWeekly.changeTerm(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(5), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));

        assertThrows(IllegalArgumentException.class,() -> scheduleWeekly.changeTerm(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(6), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));

        /*assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
*/      assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),"subota"));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",0,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",-1,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(null,LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));

        /*assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
*/      assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room(null,30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",0,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",-1,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),"subota"));
        assertThrows(IllegalArgumentException.class, () -> scheduleWeekly.changeTerm(new Term(new Room("Raf8",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf2",30,null),new Time(null,LocalDate.now().minusWeeks(3), sad.minusHours(3),120)),Time.getWeekDay(LocalDate.now())));


        for(Term t : scheduleWeekly.getTerms())
            System.out.println(t);
        System.out.println("\n");

        scheduleWeekly.changeTerm(new Term(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf4",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().plusWeeks(2), sad.minusHours(4),120)),Time.getWeekDay(LocalDate.now()));

        /*scheduleWeekly.changeTerm(new Term(new Room("Raf2",30,null),new Time(LocalDate.now().minusWeeks(4),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                , new LittleTerm(new Room("Raf3",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(0), sad.minusHours(3),120)),"Sunday");

*/
        for(Term t : scheduleWeekly.getTerms())
            System.out.println(t + ", " + Time.getWeekDay(t.getTime().getStartDate()));
    }

}
