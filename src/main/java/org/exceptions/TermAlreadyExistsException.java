package org.exceptions;

public class TermAlreadyExistsException extends Exception{
    public TermAlreadyExistsException() {
        super("Term already exists.");
    }
}
