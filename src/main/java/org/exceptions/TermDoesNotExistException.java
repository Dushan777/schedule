package org.exceptions;

public class TermDoesNotExistException extends Exception{
    public TermDoesNotExistException() {
        super("Term does not exist.");
    }
}
