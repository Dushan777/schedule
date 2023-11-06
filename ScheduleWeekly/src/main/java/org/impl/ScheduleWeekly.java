package org.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.exceptions.TermAlreadyExistsException;
import org.model.LittleTerm;
import org.model.ScheduleSpecification;
import org.model.Term;
import org.model.Time;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        if(term.getTime().getStartDate().isBefore(getBeginningDate()) || term.getTime().getEndDate().isAfter(getEndingDate()))
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
            if(getExcludedDays().contains(newTerm.getTime().getStartDate()))
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
    public void saveAsJSON(String filepath, String fileName) throws IOException {

    }

    @Override
    public void saveAsCSV(String filepath, String fileName) throws IOException {

    }

    @Override
    public void saveAsPDF(String filepath, String fileName) throws IOException {

    }

    @Override
    public void loadFromJSON(String filename) throws IOException {

    }

    @Override
    public void loadFromCSV(String filename) throws IOException {

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
