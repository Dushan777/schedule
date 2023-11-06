package org.model;

import java.util.Comparator;

public class TermComparator implements Comparator<Term> {


    @Override
    public int compare(Term o1, Term o2) {

        int comparison = o1.getRoom().getName().compareTo(o2.getRoom().getName());

        if(comparison != 0)
            return comparison;

        comparison = o1.getTime().getStartDate().compareTo(o2.getTime().getStartDate());
        if(comparison != 0)
            return comparison;

        return  o1.getTime().getStartTime().compareTo(o2.getTime().getStartTime());

    }

}
