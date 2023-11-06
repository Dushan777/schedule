package org.example;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.exceptions.DifferentDateException;
import org.exceptions.TermAlreadyExistsException;
import org.exceptions.TermDoesNotExistException;
import org.model.LittleTerm;
import org.model.ScheduleSpecification;
import org.model.Term;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    @Override
    public void initialize(LocalDate date, LocalDate date1, List<LocalDate> list) {
        setExcludedDays(list);
        setBeginningDate(date);
        setEndingDate(date1);
    }
    // TESTIRANO
    @Override
    public void addTerm(Term term, String weekDay) throws TermAlreadyExistsException, DifferentDateException {
        if(term.hasNULL())
            throw new IllegalArgumentException("Invalid term!");
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
        if(getExcludedDays().contains(term.getTime().getStartDate()))
            return false;
        if(term.getTime().getStartDate().isBefore(getBeginningDate()) || term.getTime().getStartDate().isAfter(getEndingDate()))
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
