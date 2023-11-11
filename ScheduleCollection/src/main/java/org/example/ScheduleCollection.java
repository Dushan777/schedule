package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.serializer.CollectionSerializer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleCollection extends ScheduleSpecification {

    static {
        ScheduleManager.setScheduleSpecification(new ScheduleCollection());
    }
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
        FileWriter fileWriter = new FileWriter(fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setDateFormat(new SimpleDateFormat("MM-dd-yyyy"));
        mapper.registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer(Term.class, new CollectionSerializer());
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.writeValue(fileWriter, terms);

        fileWriter.close();

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
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 800);
        contentStream.showText("Schedule information");
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(0, -20);
        int termCounter = 0;

        for (Term term : terms) {
            if (termCounter == 35) {
                // Start a new page
                contentStream.endText();
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 800);
                termCounter = 0;
            }

            String termData = term.getRoom().getName() + "," + term.getRoom().getCapacity() + "," + term.getTime().getStartDate() + ","
                    + term.getTime().getStartTime() + "," + term.getTime().getEndTime() + "," + term.getAdditionalData();
            contentStream.showText(termData);
            contentStream.newLineAtOffset(0, -15);
            termCounter++;
        }

        contentStream.endText();
        contentStream.close();
        document.save(filePath);
        document.close();
    }

    @Override
    public void loadFromJSON(String fileName, String configPath) throws IOException, DifferentDateException, TermAlreadyExistsException {
        List<ConfigMapping> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        for(ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }
        columnMappings.sort(Comparator.comparingInt(ConfigMapping::getIndex));
        FileReader fileReader = new FileReader(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(mappings.get(-2));

        List<JsonNode> jsonNodes = objectMapper.readValue(fileReader, new TypeReference<List<JsonNode>>() {});

        for(JsonNode jsonNode : jsonNodes)
        {
            Term term = new Term();
            for(ConfigMapping entry : columnMappings)
            {
                int columnIndex = entry.getIndex();

                if(columnIndex == -1 || columnIndex == -2) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "name":
                        term.setRoom(new Room(jsonNode.get(columnName).asText(), 0, null));
                        break;
                    case "capacity":
                        term.getRoom().setCapacity(jsonNode.get(columnName).asInt());
                        break;
                    case "equipment":
                        term.getRoom().getEquipment().put(columnName, jsonNode.get(columnName).asInt());
                        break;

                    case "start":
                        LocalDate startDate = LocalDate.parse(jsonNode.get(columnName).asText(), formatter);
                        term.setTime(new Time(startDate, startDate, null, null));
                        break;
                    /*case "end":
                        LocalDate endDate = LocalDate.parse(jsonNode.get(columnIndex).asText(), formatter);
                        term.getTime().setEndDate(endDate);
                        break;*/
                    case "startTime":
                        LocalTime startTime = LocalTime.parse(jsonNode.get(columnName).asText(), formatter2);
                        term.getTime().setStartTime(startTime);
                        break;
                    case "endTime":
                        LocalTime endTime = LocalTime.parse(jsonNode.get(columnName).asText(), formatter2);
                        term.getTime().setEndTime(endTime);
                        break;
                    case "additionalData":
                        term.getAdditionalData().put(columnName, jsonNode.get(columnName).asText());
                        break;
                }
            }
            addTerm(term, Time.getWeekDay(term.getTime().getStartDate()));
        }


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

                if(columnIndex == -1 || columnIndex == -2) continue;

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


}
