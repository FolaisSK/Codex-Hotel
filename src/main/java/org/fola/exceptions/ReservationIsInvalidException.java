package org.fola.exceptions;

public class ReservationIsInvalidException extends RuntimeException {
    public ReservationIsInvalidException(String message) {
        super(message);
    }
}
