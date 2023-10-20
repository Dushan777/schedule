package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ScheduleSpecification {

    private List<Term> terms = new ArrayList<>();
    /*
    - inicijalizacija rasporeda
    - dodavanje prostorija sa osobinama (kapacitet, računari, projektor)
    - dodavanje novog termina uz provere o zauzetost, obraditi situaciju da je termin već zauzet
    - brisanje zauzetog termina
    - premeštanje termina - brisanje i dodavanje novog termina sa istim vezanim podacima
    */
    public abstract void initialize();
    public abstract void addRoom(int capacity, Map<String, Integer> equipment);
    // TODO: brisanje prostorija ili izmena
    public abstract void addTerm(Term term);
    public abstract void deleteTerm(Term term);
    public abstract void changeTerm(Term oldTerm, Term newTerm);

}
