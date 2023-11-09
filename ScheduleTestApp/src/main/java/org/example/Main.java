package org.example;

import org.exceptions.DifferentDateException;
import org.exceptions.TermAlreadyExistsException;
import org.impl.ScheduleWeekly;
import org.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScheduleSpecification schedule = new ScheduleCollection();
        LocalTime sad = LocalTime.of(12, 0);
        List<Term> termini = schedule.getTerms();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unesite ime fajla(bez \".txt\") sa podacima o ucionicama, datumima trajanja rasporeda i iskljucenim danima:");
        while(true) {
            try {
                String path = scanner.nextLine();
                schedule.initialize("ScheduleTestApp/src/main/resources/" + path + ".txt");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        String line1;
        String[] splitLine;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        int size;
        while(true)
        {
            System.out.println("Choose an option : \n");
            System.out.println("1. Add room"); // RAF1,30,Racunar,2,Projektor,1
            System.out.println("2. Add term");//RAF1,30,11/23/2023,01/09/2024,09:00,11:00,profesor,urosh,asistent,dushan,Wednesday
            System.out.println("3. Delete term");//RAF1,30,11/15/2023,11/29/2023,09:15,11:15,Wednesday
            System.out.println("4. Change term");
            System.out.println("5. Check if available");//RAF1,30,11/15/2023,11/29/2023,09:15,11:15,Wednesday
            System.out.println("6. All free terms");//
            System.out.println("7. All booked terms");//
            System.out.println("8. Filter by room"); // true,RAF1,30,Racunar,2,Projektor,1
            System.out.println("9. Filter by time or additional data");//false,01/01/2024,01/13/2024,09:15,11:15,null,Wednesday
            System.out.println("10. Filter by anything");//
            System.out.println("11. Load from CSV");// termini1,config1
            System.out.println("12. Load from JSON");
            System.out.println("13. Save to CSV");//   exportCSV
            System.out.println("14. Save to JSON");//   exportJSON
            System.out.println("15. Save to PDF");//   exportPDF
            System.out.println("0. Exit");//
            scanner = new Scanner(System.in);
            int option;
            while(true) {
                try {
                    option = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
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
                    termini = schedule.getTerms();
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    size = splitLine.length;
                    for(int i = 0 ; i < splitLine.length; i++)
                    {
                        if(splitLine[i].equals("null"))
                            splitLine[i] = null;
                    }
                    Map<String, String> h1 = new HashMap<>();
                    try {
                        if(size >=9 && size%2==1) {
                            for (int i = 6; i < size - 1; i += 2) {
                                h1.put(splitLine[i], splitLine[i + 1]);
                            }
                            try {
                                schedule.addTerm(new Term(new Room(splitLine[0], Integer.parseInt(splitLine[1]), getRoomByName(splitLine[0], schedule).getEquipment()), new Time(LocalDate.parse(splitLine[2], formatter), LocalDate.parse(splitLine[3], formatter), LocalTime.parse(splitLine[4], formatter1), LocalTime.parse(splitLine[5], formatter1)), h1), splitLine[size - 1]);
                            }
                            catch (NullPointerException e)
                            {
                                System.out.println("Invalid room!");
                            }
                        }
                        else if(size == 8) {
                            try {
                                schedule.addTerm(new Term(new Room(splitLine[0], Integer.parseInt(splitLine[1]), getRoomByName(splitLine[0], schedule).getEquipment()), new Time(LocalDate.parse(splitLine[2], formatter), LocalDate.parse(splitLine[3], formatter), LocalTime.parse(splitLine[4], formatter1), LocalTime.parse(splitLine[5], formatter1)), null), splitLine[size - 1]);
                            }
                            catch (NullPointerException e)
                            {
                                System.out.println("Invalid room!");
                            }
                        }
                        else
                        {
                            System.out.println("Invalid input");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Delete term");
                    termini = schedule.getTerms();
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    size = splitLine.length;
                    try {
                        if(size == 7)
                        {
                            schedule.deleteTerm(new Term(new Room(splitLine[0],Integer.parseInt(splitLine[1]),null),new Time(LocalDate.parse(splitLine[2],formatter),LocalDate.parse(splitLine[3],formatter),LocalTime.parse(splitLine[4],formatter1),LocalTime.parse(splitLine[5],formatter1)),null),splitLine[6]);
                        }
                        else
                        {
                            System.out.println("Invalid input");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Change term");
                    break;
                case 5:
                    System.out.println("Check if available");
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    size = splitLine.length;
                    try {
                        if(size == 7)
                        {
                            boolean rez = schedule.termAvailable(new Term(new Room(splitLine[0],Integer.parseInt(splitLine[1]),null),new Time(LocalDate.parse(splitLine[2],formatter),LocalDate.parse(splitLine[3],formatter),LocalTime.parse(splitLine[4],formatter1),LocalTime.parse(splitLine[5],formatter1)),null),splitLine[6]);
                            System.out.println(rez);
                        }
                        else
                        {
                            System.out.println("Invalid input");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("All free terms");
                    termini = schedule.allFreeTerms();
                    termini.forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("All booked terms");
                    termini = schedule.getTerms();
                    termini.forEach(System.out::println);
                    break;
                case 8:
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
                    Map<String, Integer> h2 = new HashMap<>();
                    try {
                        if(size >= 5 && size%2==1)
                        {
                            for(int i = 3; i < size; i+=2)
                            {
                                h2.put(splitLine[i],Integer.parseInt(splitLine[i+1]));
                            }
                            termini = schedule.filterByRooms(splitLine[1],Integer.parseInt(splitLine[2]),h2,Boolean.parseBoolean(splitLine[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size == 4 && splitLine[3] == null)
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
                case 9:
                    System.out.println("Filter by time or additional data");
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    splitLine = line1.split(",");
                    size = splitLine.length;
                    for(int i = 0 ; i < splitLine.length; i++)
                    {
                        if(splitLine[i].equals("null"))
                            splitLine[i] = null;

                    }
                    if(splitLine[1] == null) {
                        String m,d, y;
                        m = String.valueOf(schedule.getBeginningDate().getMonth().getValue());
                        d = String.valueOf(schedule.getBeginningDate().getDayOfMonth());
                        y = String.valueOf(schedule.getBeginningDate().getYear());
                        if(Integer.parseInt(d) < 10)
                            d = "0"+d;
                        if(Integer.parseInt(m) < 10)
                            m = "0"+m;
                        splitLine[1] = m+"/"+d+"/"+y;
                    }
                    if(splitLine[2] == null) {
                        String m,d, y;
                        m = String.valueOf(schedule.getEndingDate().getMonth().getValue());
                        d = String.valueOf(schedule.getEndingDate().getDayOfMonth());
                        y = String.valueOf(schedule.getEndingDate().getYear());
                        if(Integer.parseInt(d) < 10)
                            d = "0"+d;
                        if(Integer.parseInt(m) < 10)
                            m = "0"+m;
                        splitLine[2] = m+"/"+d+"/"+y;
                    }
                    if(splitLine[3] == null)
                        splitLine[3] = "00:00";
                    if(splitLine[4] == null)
                        splitLine[4] = "23:59";
                    Map<String, String> h3 = new HashMap<>();
                    try {
                        if(size >= 8 && size%2==0)
                        {
                            for(int i = 5; i < size; i+=2)
                            {
                                h3.put(splitLine[i],splitLine[i+1]);
                            }
                           termini = schedule.filterByTimeOrAdditionalData(new Time(LocalDate.parse(splitLine[1],formatter),LocalDate.parse(splitLine[2],formatter),LocalTime.parse(splitLine[3],formatter1),LocalTime.parse(splitLine[4],formatter1)),h3,splitLine[6],Boolean.parseBoolean(splitLine[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size == 7 && splitLine[5] == null)
                        {
                            termini = schedule.filterByTimeOrAdditionalData(new Time(LocalDate.parse(splitLine[1],formatter),LocalDate.parse(splitLine[2],formatter),LocalTime.parse(splitLine[3],formatter1),LocalTime.parse(splitLine[4],formatter1)),null,splitLine[6],Boolean.parseBoolean(splitLine[0]));
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
                case 10:
                    System.out.println("Filter by anything");
                    scanner = new Scanner(System.in);
                    line1 = scanner.nextLine();
                    if(!line1.contains(" "))
                    {
                        System.out.println("Invalid input");
                        break;
                    }
                    splitLine = line1.split(" ");
                    String splitL = splitLine[0];
                    String splitL2 = splitLine[1];
                    String[] splitLine2 = splitL.split(",");
                    String[] splitLine3 = splitL2.split(",");

                    size = splitLine2.length;
                    int size2 = splitLine3.length;

                    for(int i = 0 ; i < splitLine2.length; i++)
                    {
                        if(splitLine2[i].equals("null"))
                            splitLine2[i] = null;

                    }
                    for(int i = 0 ; i < splitLine3.length; i++)
                    {
                        if(splitLine3[i].equals("null"))
                            splitLine3[i] = null;

                    }

                    if(splitLine3[0] == null) {
                        String m,d, y;
                        m = String.valueOf(schedule.getBeginningDate().getMonth().getValue());
                        d = String.valueOf(schedule.getBeginningDate().getDayOfMonth());
                        y = String.valueOf(schedule.getBeginningDate().getYear());
                        if(Integer.parseInt(d) < 10)
                            d = "0"+d;
                        if(Integer.parseInt(m) < 10)
                            m = "0"+m;
                        splitLine3[0] = m+"/"+d+"/"+y;
                    }
                    if(splitLine3[1] == null) {
                        String m,d, y;
                        m = String.valueOf(schedule.getEndingDate().getMonth().getValue());
                        d = String.valueOf(schedule.getEndingDate().getDayOfMonth());
                        y = String.valueOf(schedule.getEndingDate().getYear());
                        if(Integer.parseInt(d) < 10)
                            d = "0"+d;
                        if(Integer.parseInt(m) < 10)
                            m = "0"+m;
                        splitLine3[1] = m+"/"+d+"/"+y;
                    }
                    if(splitLine3[2] == null)
                        splitLine3[2] = "00:00";
                    if(splitLine3[3] == null)
                        splitLine3[3] = "23:59";

                    Map<String, Integer> h4 = new HashMap<>();
                    Map<String, String> h5 = new HashMap<>();

                    try {
                        if(size >= 5 && size%2==1)
                        {
                            for(int i = 3; i < size; i+=2)
                            {
                                h4.put(splitLine2[i], Integer.valueOf(splitLine2[i+1]));
                            }

                        }
                        if(size2 >= 7 && size2%2==1)
                        {
                            for(int i = 4; i < size2; i+=2)
                            {
                                h5.put(splitLine3[i], splitLine3[i+1]);
                            }

                        }
                        if(size2 == 6 && splitLine3[4] == null && size == 4 && splitLine2[3] == null)
                        {
                            termini = schedule.filterByEverything(splitLine2[1], Integer.parseInt(splitLine2[2]),null,new Time(LocalDate.parse(splitLine3[0],formatter),LocalDate.parse(splitLine3[1],formatter),LocalTime.parse(splitLine3[2],formatter1),LocalTime.parse(splitLine3[3],formatter1)),null,splitLine3[5],Boolean.parseBoolean(splitLine2[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size2 >= 7 && size2%2==1 && size == 4 && splitLine2[3] == null)
                        {
                            termini = schedule.filterByEverything(splitLine2[1], Integer.parseInt(splitLine2[2]),null,new Time(LocalDate.parse(splitLine3[0],formatter),LocalDate.parse(splitLine3[1],formatter),LocalTime.parse(splitLine3[2],formatter1),LocalTime.parse(splitLine3[3],formatter1)),h5,splitLine3[5],Boolean.parseBoolean(splitLine2[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size2 == 6 && splitLine3[4] == null && size >= 5 && size%2==1)
                        {
                            termini = schedule.filterByEverything(splitLine2[1], Integer.parseInt(splitLine2[2]),h4,new Time(LocalDate.parse(splitLine3[0],formatter),LocalDate.parse(splitLine3[1],formatter),LocalTime.parse(splitLine3[2],formatter1),LocalTime.parse(splitLine3[3],formatter1)),null,splitLine3[5],Boolean.parseBoolean(splitLine2[0]));
                            termini.forEach(System.out::println);
                        }
                        else if(size2 >= 7 && size2%2==1 && size >= 5 && size%2==1)
                        {
                            termini = schedule.filterByEverything(splitLine2[1], Integer.parseInt(splitLine2[2]),h4,new Time(LocalDate.parse(splitLine3[0],formatter),LocalDate.parse(splitLine3[1],formatter),LocalTime.parse(splitLine3[2],formatter1),LocalTime.parse(splitLine3[3],formatter1)),h5,splitLine3[5],Boolean.parseBoolean(splitLine2[0]));
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
                case 11:
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla i config fajla u formatu: imeFajla,imeConfigFajla. Ne unositi ekstenzije!");
                        String line = scanner.nextLine();
                        schedule.loadFromCSV("ScheduleTestApp/src/main/resources/"+line.split(",")[0]+".csv",
                                "ScheduleTestApp/src/main/resources/"+line.split(",")[1]+".txt");
                        for(Term t : schedule.getTerms())
                            System.out.println(t);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (DifferentDateException  | TermAlreadyExistsException e)
                    {
                        System.out.println("Podaci iz fajla su nevalidni!");
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        System.out.println("Morate uneti 2 fajla");

                    }
                    break;
                case 12:
                    System.out.println("Load from JSON");
                    break;
                case 13:
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla");
                        String line = scanner.nextLine();
                        schedule.saveAsCSV(termini,"ScheduleTestApp/src/main/resources/"+line+".csv");

                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 14:
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla");
                        String line = scanner.nextLine();
                        schedule.saveAsJSON(termini,"ScheduleTestApp/src/main/resources/"+line+".json");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 15:
                    System.out.println("Save to PDF");
                    //TODO: dodati nove stranice na npr. 20 termina
                    try {
                        scanner = new Scanner(System.in);
                        System.out.println("Unesite ime fajla");
                        String line = scanner.nextLine();
                        schedule.saveAsPDF(termini,"ScheduleTestApp/src/main/resources/"+line+".pdf");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
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
    public static Room getRoomByName(String name, ScheduleSpecification schedule)
    {
        for(Room r : schedule.getRooms())
        {
            if(r.getName().equals(name))
                return r;
        }
        return null;
    }
}