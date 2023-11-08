package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.exceptions.DifferentDateException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;
import org.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleCollection extends ScheduleSpecification {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean change = false;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean revert = false;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Term termToRevert;

    // TESTIRANO
    @Override
    public void addTerm(Term term, String weekDay) throws TermAlreadyExistsException, DifferentDateException, IllegalArgumentException {
        if(term.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        if(!getRooms().contains(term.getRoom()))
            throw new IllegalArgumentException("Invalid room!");
        Room room = getRooms().get(getRooms().indexOf(term.getRoom()));
        if(room.getCapacity() != term.getRoom().getCapacity())
            throw new IllegalArgumentException("Invalid room!");
        if(!term.getTime().getStartDate().equals(term.getTime().getEndDate())) {
            if(change && revert)
            {
                getTerms().add(termToRevert);
                revert = false;
                change = false;
            }
            throw new DifferentDateException();
        }
        if(!termAvailable(term, weekDay)) {
            if(change && revert)
            {
                getTerms().add(termToRevert);
                revert = false;
                change = false;
            }
            throw new TermAlreadyExistsException();
        }
        else
            getTerms().add(term);
    }

    // TESTIRANO
    @Override
    public boolean termAvailable(Term term, String weekDay) {
        if(term.hasNULL())
            return false;
        if(getExcludedDays() != null && getExcludedDays().contains(term.getTime().getStartDate()))
            return false;
        if(getBeginningDate() != null  && getEndingDate() != null
                && ((term.getTime().getStartDate().isBefore(getBeginningDate()) || term.getTime().getEndDate().isAfter(getEndingDate()))
        || (term.getTime().getEndDate().isBefore(getBeginningDate()) || term.getTime().getStartDate().isAfter(getEndingDate()))))
            return false;
        if(!term.getTime().getStartDate().equals(term.getTime().getEndDate()))
            return false;
        for(Term t : getTerms()) {
            if(termsOverlap(t, term))
                return false;
        }
        return true;
    }

    // TESTIRANO
    @Override
    public void deleteTerm(Term term, String weekDay) throws TermDoesNotExistException{
        if(term.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        for(Term t : getTerms()) {
            if(t.equals(term)) {
                getTerms().remove(t);
                if(change) {
                    revert = true;
                    termToRevert = t;
                }
                return;
            }
        }
        revert = false;
        change = false;
        throw new TermDoesNotExistException();
    }

    // za test: jedan normalan, jedan gde nema sta da obrise, jedan gde je novi termin zauzet

    /*
    t t
    f u
    t f
     */

    // TESTIRANO
    @Override
    public void changeTerm(Term term, LittleTerm term1, String weekDay) throws TermDoesNotExistException, TermAlreadyExistsException, DifferentDateException, IllegalArgumentException {
        if(term.hasNULL() || term1.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
        if(term.getTime().getStartDate().equals(term.getTime().getEndDate()) &&
                term1.getTime().getStartDate().equals(term1.getTime().getEndDate())) {
            change = true;
            deleteTerm(term, weekDay);
            Term newTerm = new Term(term1.getRoom(), term1.getTime(), term.getAdditionalData());
            addTerm(newTerm, weekDay);
            change = false;
            revert = false;
        }
        else
            throw new IllegalArgumentException("Invalid date!");
    }

    @Override
    public void saveAsJSON(List<Term> terms, String fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (PrintWriter writer = new PrintWriter(fileName + ".json")) {
            writer.println(gson.toJson(terms));
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }

    }
    @Override
    public void saveAsCSV(List<Term> terms,String filePath) throws IOException {

        FileWriter fileWriter = new FileWriter(filePath);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        for (Term term : terms) {
            csvPrinter.printRecord(
                    term.getRoom().getName(),
                    term.getRoom().getCapacity(),
                    term.getTime().getStartDate(),
                    term.getTime().getStartTime(),
                    term.getTime().getEndTime(),
                    term.getAdditionalData(),
                    term.getRoom().getEquipment()
            );
        }

        csvPrinter.close();
        fileWriter.close();
    }

    @Override
    public void saveAsPDF(List<Term> terms,String filePath) throws IOException {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            // Export Student Information
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 600);
            contentStream.showText("Schedule information");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(0, -20);
            //contentStream.beginText();
            String termsHeaders = "ImeUcionice,Kapacitet,Pocetak,PocetakCasa,KrajCasa,Dodatno";
            for (Term term : terms) {
                String termData = term.getRoom().getName()+","+term.getRoom().getCapacity()+","+term.getTime().getStartDate()+","+term.getTime().getStartTime()+","+term.getTime().getEndTime()+","+term.getAdditionalData();
                contentStream.showText(termData);
                contentStream.newLineAtOffset(0, -15);
            }
            contentStream.endText();
            contentStream.close();
            document.save(filePath);
            document.close();
    }

    @Override
    public void loadFromJSON(String fileName) throws IOException {

    }

    @Override
    public void loadFromCSV(String fileName, String configPath) throws IOException, DifferentDateException, TermAlreadyExistsException {
        List<ConfigMapping> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }
        FileReader fileReader = new FileReader(fileName);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(mappings.get(-2));
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
                        term.setTime(new Time(startDate, startDate, null, null));
                        break;
                    /*case "end":
                        LocalDate endDate = LocalDate.parse(record.get(columnIndex), formatter);
                        term.getTime().setEndDate(endDate);
                        break;*/
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
                }
            }

            addTerm(term, Time.getWeekDay(term.getTime().getStartDate()));
        }
    }



    /*private boolean termsOverlap(Term t, Term term) {
        if(!t.getTime().getStartDate().equals(term.getTime().getStartDate()))
            return false;
        if (t.getRoom().equals(term.getRoom()))
            return !((t.getTime().getEndTime().isBefore(term.getTime().getStartTime()) || t.getTime().getStartTime().isAfter(term.getTime().getEndTime())
            || t.getTime().getEndTime().equals(term.getTime().getStartTime()) || t.getTime().getStartTime().equals(term.getTime().getEndTime())));
        return false;
    }*/
}
