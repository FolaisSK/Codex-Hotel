package org.fola.exceptions;

public class InvalidRoomNumberException extends RuntimeException {
    public InvalidRoomNumberException(String message) {
        super(message);
    }
}
