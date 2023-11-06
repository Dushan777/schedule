package org.exceptions;

public class DifferentDateException extends Exception{
    public DifferentDateException() {
        super("Start date and end date are different!");
    }
}
