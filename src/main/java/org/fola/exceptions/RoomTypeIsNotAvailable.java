package org.fola.exceptions;

public class RoomTypeIsNotAvailable extends RuntimeException {
    public RoomTypeIsNotAvailable(String message) {
        super(message);
    }
}
