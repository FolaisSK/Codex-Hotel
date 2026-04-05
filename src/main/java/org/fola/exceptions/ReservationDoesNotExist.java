package org.fola.exceptions;

public class ReservationDoesNotExist extends RuntimeException {
    public ReservationDoesNotExist(String message) {
        super(message);
    }
}
