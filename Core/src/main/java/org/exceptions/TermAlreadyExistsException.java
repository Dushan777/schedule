package org.exceptions;

public class TermAlreadyExistsException extends Exception{
    public TermAlreadyExistsException() {
        super("Term already exists, overlaps with another term or is not within the duration of the schedule.");
    }
}
