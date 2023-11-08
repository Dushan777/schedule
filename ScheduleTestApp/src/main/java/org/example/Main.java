package org.example;

import org.exceptions.DifferentDateException;
import org.exceptions.TermAlreadyExistsException;
import org.impl.ScheduleWeekly;
import org.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScheduleSpecification schedule = new ScheduleWeekly();
        LocalTime sad = LocalTime.of(12, 0);
        List<Term> termini = null;
       schedule.initialize(LocalDate.now().minusWeeks(4), LocalDate.now().plusWeeks(4),null);
        try {
            schedule.addRoom("RAF1",30, Map.of("Racunar", 2));
           /* schedule.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(2), sad.minusHours(4), 120), null), Time.getWeekDay(LocalDate.now()));
            schedule.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(1), sad.minusHours(2), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1)));
            schedule.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(0), sad.minusHours(0), 120), null), Time.getWeekDay(LocalDate.now()));
            schedule.addTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(4), LocalDate.now().minusWeeks(1), sad.minusHours(-2), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1)));
            schedule.deleteTerm(new Term(new Room("Raf1", 30, null), new Time(LocalDate.now().minusWeeks(3), LocalDate.now().minusWeeks(1), sad.minusHours(-2), 120), null), Time.getWeekDay(LocalDate.now().minusDays(1)));
            termini = schedule.allFreeTerms();

            schedule.changeTerm(new Term(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(2),LocalDate.now().minusWeeks(2), sad.minusHours(4),120),null)
                    , new LittleTerm(new Room("Raf1",30,null),new Time(LocalDate.now().minusWeeks(1),LocalDate.now().minusWeeks(-1), sad.minusHours(7),120)),Time.getWeekDay(LocalDate.now()));
       */ } catch (Exception e) {
            throw new RuntimeException(e);
        }
 /*
        for(Term t : schedule.getTerms())
            System.out.println(t);
        System.out.println(schedule.getTerms().size());*/
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.println("Choose an option : \n");
            System.out.println("1. Initialize schedule");
            System.out.println("2. Add room");
            System.out.println("3. Add term");
            System.out.println("4. Delete term");
            System.out.println("5. Change term");
            System.out.println("6. All free terms");
            System.out.println("7. All booked terms");
            System.out.println("8. Filter by room");
            System.out.println("9. Filter by time");
            System.out.println("10. Filter by anything");
            System.out.println("11. Load from CSV");
            System.out.println("12. Load from JSON");
            System.out.println("13. Save to CSV");
            System.out.println("14. Save to JSON");
            System.out.println("15. Save to PDF");
            System.out.println("0. Exit");
            int option = scanner.nextInt();
            switch (option)
            {
                case 1:
                    System.out.println("Initialize schedule");
                    break;
                case 2:
                    System.out.println("Add room");
                    break;
                case 3:
                    System.out.println("Add term");
                    break;
                case 4:
                    System.out.println("Delete term");
                    break;
                case 5:
                    System.out.println("Change term");
                    break;
                case 6:
                    System.out.println("All free terms");
                    break;
                case 7:
                    System.out.println("All booked terms");
                    break;
                case 8:
                    System.out.println("Filter by room");
                    break;
                case 9:
                    System.out.println("Filter by time");
                    break;
                case 10:
                    System.out.println("Filter by anything");
                    break;
                case 11:
                    try {
                        Scanner scanner1 = new Scanner(System.in);
                        System.out.println("Unesite ime fajla i config fajla u formatu: imeFajla,imeConfigFajla");
                        String line = scanner1.nextLine();
                        schedule.loadFromCSV("ScheduleTestApp/src/main/resources/"+line.split(",")[0]+".csv",
                                "ScheduleTestApp/src/main/resources/"+line.split(",")[1]+".txt");
                        for(Term t : schedule.getTerms())
                            System.out.println(t);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (DifferentDateException  | TermAlreadyExistsException e)
                    {
                        System.out.println("Podaci iz fajla su nevalidni!");

                    }
                    break;
                case 12:
                    System.out.println("Load from JSON");
                    break;
                case 13:
                    try {
                        Scanner scanner1 = new Scanner(System.in);
                        System.out.println("Unesite ime fajla");
                        String line = scanner1.nextLine();
                        schedule.saveAsCSV(schedule.getTerms(),"ScheduleTestApp/src/main/resources/"+line+".csv");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    System.out.println("Save to JSON");
                    break;
                case 15:
                    System.out.println("Save to PDF");
                    break;

                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }


    }
}