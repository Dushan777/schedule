package org.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.exceptions.TermAlreadyExistsException;
import org.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleWeekly extends ScheduleSpecification {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean revert = false;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean change = false;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Term> revertedTerms = new ArrayList<>();
    @Override
    public void initialize(LocalDate date, LocalDate date1, List<LocalDate> list) {
        setExcludedDays(list);
        setBeginningDate(date);
        setEndingDate(date1);
    }

    // TESTIRANO
    @Override
    public void addTerm(Term term, String weekDay) throws TermAlreadyExistsException, IllegalArgumentException {
        if(term.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        if(!getRooms().contains(term.getRoom()))
            throw new IllegalArgumentException("Invalid room!");
        if(weekDay == null || weekDay.equals(""))
            throw new IllegalArgumentException("Invalid week day!");
        if(!weekDay.equals("Monday") && !weekDay.equals("Tuesday") && !weekDay.equals("Wednesday") && !weekDay.equals("Thursday") && !weekDay.equals("Friday") && !weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
            throw new IllegalArgumentException("Invalid week day!");
        LocalDate date = term.getTime().getStartDate();
        LocalDate date1 = term.getTime().getEndDate();
        List<Term> termsToAdd = new ArrayList<>();
        while(!date.isAfter(date1))
        {
            if(Time.getWeekDay(date).equals(weekDay))
            {
                Term newTerm = new Term(term.getRoom(), new Time(date, date, term.getTime().getStartTime(), term.getTime().getEndTime()), term.getAdditionalData());
                if(!termAvailable(newTerm, weekDay)) {
                    if(revert && change) {       // za slucaj da ne uspe da doda termin, vraca sve termine koje je obrisao pri promeni
                        getTerms().addAll(revertedTerms);
                        revertedTerms.clear();
                    }
                    change = false;
                    revert = false;
                    throw new TermAlreadyExistsException();
                }
                else
                    termsToAdd.add(newTerm);
            }
            date = date.plusDays(1);
        }
        getTerms().addAll(termsToAdd);

    }

    // TESTIRANO
    @Override
    public boolean termAvailable(Term term, String weekDay) {
        if(term.hasNULL())
            return false;
        if(weekDay == null || weekDay.equals(""))
            return false;
        if(!weekDay.equals("Monday") && !weekDay.equals("Tuesday") && !weekDay.equals("Wednesday") && !weekDay.equals("Thursday") && !weekDay.equals("Friday") && !weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
            return false;
        if(getBeginningDate() != null  && term.getTime().getStartDate().isBefore(getBeginningDate()) && getEndingDate() != null && term.getTime().getStartDate().isAfter(getEndingDate()))
            return false;
        /*if(term.getTime().getStartDate().isAfter(getEndingDate()) || term.getTime().getEndDate().isBefore(getBeginningDate()))
            return false;*/
        LocalDate date = term.getTime().getStartDate();
        LocalDate date1 = term.getTime().getEndDate();
        if(date.isAfter(date1))
            return false;
        while(!date.isAfter(date1))
        {
            if(!Time.getWeekDay(date).equals(weekDay))
            {
                date = date.plusDays(1);
                continue;
            }
            Term newTerm = new Term(term.getRoom(), new Time(date, date, term.getTime().getStartTime(), term.getTime().getEndTime()), term.getAdditionalData());
            if(getExcludedDays() != null && getExcludedDays().contains(newTerm.getTime().getStartDate()))
                return false;
            for(Term t : getTerms()) {
                if(termsOverlap(t, newTerm))
                    return false;
            }
            date = date.plusDays(1);
        }
        return true;
    }

    // TESTIRANO
    @Override
    public void deleteTerm(Term term, String weekDay) throws IllegalArgumentException{
        if(term.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        if(weekDay == null || weekDay.equals(""))
            throw new IllegalArgumentException("Invalid week day!");
        if(!weekDay.equals("Monday") && !weekDay.equals("Tuesday") && !weekDay.equals("Wednesday") && !weekDay.equals("Thursday") && !weekDay.equals("Friday") && !weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
            throw new IllegalArgumentException("Invalid week day!");
        LocalDate date = term.getTime().getStartDate();
        LocalDate date1 = term.getTime().getEndDate();
        // ide kroz sve dane od date do date1 i brise termine
        revertedTerms.clear();
        while(!date.isAfter(date1))
        {
            if(Time.getWeekDay(date).equals(weekDay))
            {
                Term newTerm = new Term(term.getRoom(), new Time(date, date, term.getTime().getStartTime(), term.getTime().getEndTime()), term.getAdditionalData());
                boolean delete = false;
                for(Term t : getTerms()) {
                    if (t.equals(newTerm)) {
                        delete = true;
                        break;
                    }
                }
                getTerms().remove(newTerm);
                if(delete && change) {
                    revertedTerms.add(newTerm);
                    revert = true;
                }
            }
            date = date.plusDays(1);
        }
    }

    // TODO: moze da obrise sve, a da ne uspe da doda nijedan
    // TESTIRANO
    @Override
    public void changeTerm(Term term, LittleTerm term1, String weekDay) throws TermAlreadyExistsException, IllegalArgumentException{
        if(term.hasNULL() || term1.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        if(weekDay == null || weekDay.equals(""))
            throw new IllegalArgumentException("Invalid week day!");
        if(!weekDay.equals("Monday") && !weekDay.equals("Tuesday") && !weekDay.equals("Wednesday") && !weekDay.equals("Thursday") && !weekDay.equals("Friday") && !weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
            throw new IllegalArgumentException("Invalid week day!");
        if(!(term.getTime().getStartDate().isAfter(term.getTime().getEndDate()) ||
                term1.getTime().getStartDate().isAfter(term1.getTime().getEndDate()))) {
            change = true;
            deleteTerm(term, weekDay);
            Term newTerm = new Term(term1.getRoom(), term1.getTime(), term.getAdditionalData());
            addTerm(newTerm, weekDay);
            change = false;
            revert = false;
        }
        else        //TODO: promeniti ime exceptiona
            throw new IllegalArgumentException("Invalid date!");
    }

    @Override
    public void saveAsJSON(List<Term> terms, String fileName) throws IOException {

    }

    @Override
    public void saveAsCSV(List<Term> terms,String filePath) throws IOException {

        FileWriter fileWriter = new FileWriter(filePath);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);


        terms.sort(new TermComparator());
        List<Term> curr = new ArrayList<>();
        for (Term term : terms) {
            int count = 0;
            if(curr.contains(term))
                continue;
            for(Term t : terms)
            {
                if(t.equals(new Term(term.getRoom(), new Time(term.getTime().getStartDate().plusWeeks(count+1), term.getTime().getEndDate().plusWeeks(count+1), term.getTime().getStartTime(), term.getTime().getEndTime()), term.getAdditionalData())))
                {
                    count++;
                    if(!curr.contains(t))
                        curr.add(t);
                }
            }

            csvPrinter.printRecord(
                    term.getRoom().getName(),
                    term.getRoom().getCapacity(),
                    term.getTime().getStartDate(),
                    term.getTime().getStartTime(),
                    term.getTime().getEndTime(),
                    term.getTime().getEndDate().plusWeeks(count),
                    term.getAdditionalData(),
                    term.getRoom().getEquipment(),
                    Time.getWeekDay(term.getTime().getStartDate())
            );
        }

        csvPrinter.close();
        fileWriter.close();
    }

    @Override
    public void saveAsPDF(List<Term> terms,String filePath) throws IOException {

    }

    @Override
    public void loadFromJSON(String fileName) throws IOException {

    }
    private static List<ConfigMapping> readConfig(String filePath) throws FileNotFoundException {
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
    @Override
    public void loadFromCSV(String fileName, String configPath) throws IOException, TermAlreadyExistsException {
        List<ConfigMapping> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }
        FileReader fileReader = new FileReader(fileName);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(mappings.get(-2));
        String[] day = mappings.get(-3).split("/");

        String weekDay = " ";
        for (CSVRecord record : parser) {
            Term term = new Term();

            for (ConfigMapping entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if(columnIndex == -1) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "name":
                        term.setRoom(new Room(record.get(columnIndex), 0, null));
                        break;
                    case "capacity":
                        term.getRoom().setCapacity(Integer.parseInt(record.get(columnIndex)));
                        break;
                    case "equipment":
                        term.getRoom().getEquipment().put(columnName, Integer.valueOf(record.get(columnIndex)));
                        break;

                    case "start":
                        LocalDate startDate = LocalDate.parse(record.get(columnIndex), formatter);
                        term.setTime(new Time(startDate, null, null, null));
                        break;
                    case "end":
                        LocalDate endDate = LocalDate.parse(record.get(columnIndex), formatter);
                        term.getTime().setEndDate(endDate);
                        break;
                    case "startTime":
                        LocalTime startTime = LocalTime.parse(record.get(columnIndex), formatter2);
                        term.getTime().setStartTime(startTime);
                        break;
                    case "endTime":
                        LocalTime endTime = LocalTime.parse(record.get(columnIndex), formatter2);
                        term.getTime().setEndTime(endTime);
                        break;
                    case "additionalData":
                        term.getAdditionalData().put(columnName, record.get(columnIndex));
                        break;
                    case "weekDay":
                        for (String s : day)
                            if (s.equals(record.get(columnIndex)))
                                weekDay = s;
                        if(weekDay.equals(" "))
                            throw new IllegalArgumentException("Invalid week day!");
                        break;
                }
            }

            addTerm(term, weekDay);
        }
    }



    private boolean termsOverlap(Term t, Term term) {
        if(!t.getTime().getStartDate().equals(term.getTime().getStartDate()))
            return false;
        if (t.getRoom().equals(term.getRoom()))
            return !((t.getTime().getEndTime().isBefore(term.getTime().getStartTime()) || t.getTime().getStartTime().isAfter(term.getTime().getEndTime())
                    || t.getTime().getEndTime().equals(term.getTime().getStartTime()) || t.getTime().getStartTime().equals(term.getTime().getEndTime())));
        return false;
    }
}
