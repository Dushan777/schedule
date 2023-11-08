package org.example;

import org.exceptions.DifferentDateException;
import org.exceptions.TermAlreadyExistsException;
import org.impl.ScheduleWeekly;
import org.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScheduleSpecification schedule = new ScheduleWeekly();
        LocalTime sad = LocalTime.of(12, 0);
        List<Term> termini = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite ime fajla(bez \".txt\") sa podacima o ucionicama, datumima trajanja rasporeda i iskljucenim danima:");
        String path = scanner.nextLine();
        try {
            schedule.initialize("ScheduleTestApp/src/main/resources/"+path+".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            //schedule.addRoom("RAF1",30, Map.of("Racunar", 2));
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
        String line1;
        String[] splitLine;
        int size;
        while(true)
        {
            System.out.println("Choose an option : \n");
            System.out.println("1. Add room"); // RAF1,30,Racunar,2,Projektor,1
            System.out.println("2. Add term");
            System.out.println("3. Delete term");
            System.out.println("4. Change term");
            System.out.println("5. All free terms");//
            System.out.println("6. All booked terms");//
            System.out.println("7. Filter by room"); // true,RAF1,30,Racunar,2,Projektor,1
            System.out.println("8. Filter by time");
            System.out.println("9. Filter by anything");
            System.out.println("10. Load from CSV");// termini1,config1
            System.out.println("11. Load from JSON");
            System.out.println("12. Save to CSV");//   exportCSV
            System.out.println("13. Save to JSON");
            System.out.println("14. Save to PDF");
            System.out.println("0. Exit");//
            scanner = new Scanner(System.in);
            int option = Integer.parseInt(scanner.nextLine());
            switch (option)
            {
                case 1:
                    System.out.println("Add room");
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    size = splitLine.length;
                    Map<String, Integer> h = new HashMap<>();
                    try {
                        if(size >= 4 && size%2==0)
                        {
                            for(int i = 2; i < size; i+=2)
                            {
                                h.put(splitLine[i],Integer.parseInt(splitLine[i+1]));
                            }
                            schedule.addRoom(splitLine[0],Integer.parseInt(splitLine[1]),h);
                        }
                        else if(size == 2)
                        {
                            schedule.addRoom(splitLine[0],Integer.parseInt(splitLine[1]),null);
                        }
                        else
                        {
                            System.out.println("Invalid input");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Add term");
                    break;
                case 3:
                    System.out.println("Delete term");
                    break;
                case 4:
                    System.out.println("Change term");
                    break;
                case 5:
                    System.out.println("All free terms");
                    schedule.allFreeTerms().forEach(System.out::println);
                    break;
                case 6:
                    System.out.println("All booked terms");
                    schedule.getTerms().forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("Filter by room. Enter true/false(true - filter booked term, false - filter available terms),room name, capacity and equipment in format:\nname,capacity,equipment1,quantity1,equipment2,quantity2...\nIf you don't want to filter by something, enter \"null\" as it's value or 0 if it is capacity");
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    for(int i = 0 ; i < splitLine.length; i++)
                    {
                        if(splitLine[i].equals("null"))
                            splitLine[i] = null;
                    }
                    size = splitLine.length;
                    Map<String, Integer> h1 = new HashMap<>();
                    try {
                        if(size >= 5 && size%2==1)
                        {
                            for(int i = 3; i < size; i+=2)
                            {
                                h1.put(splitLine[i],Integer.parseInt(splitLine[i+1]));
                            }
                            termini = schedule.filterByRooms(splitLine[1],Integer.parseInt(splitLine[2]),h1,Boolean.parseBoolean(splitLine[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size == 4)
                        {
                            termini = schedule.filterByRooms(splitLine[1],Integer.parseInt(splitLine[2]),null,Boolean.parseBoolean(splitLine[0]));
                            termini.forEach(System.out::println);
                        }
                        else
                        {
                            System.out.println("Invalid input");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println("Filter by time or additional data");
                    break;
                case 9:
                    System.out.println("Filter by anything");
                    break;
                case 10:
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla i config fajla u formatu: imeFajla,imeConfigFajla. Ne unositi ekstenzije!");
                        String line = scanner.nextLine();
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
                case 11:
                    System.out.println("Load from JSON");
                    break;
                case 12:
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla");
                        String line = scanner.nextLine();
                        schedule.saveAsCSV(termini,"ScheduleTestApp/src/main/resources/"+line+".csv");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    System.out.println("Save to JSON");
                    break;
                case 14:
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