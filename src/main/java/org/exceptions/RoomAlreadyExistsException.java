package org.exceptions;

public class RoomAlreadyExistsException extends Exception {
    public RoomAlreadyExistsException() {
        super("Room already exists.");
    }
}
