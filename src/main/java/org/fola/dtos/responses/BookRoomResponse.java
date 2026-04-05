package org.fola.dtos.responses;

import lombok.Data;
import org.fola.data.models.Reservation;
import org.fola.data.models.Type;

@Data
public class BookRoomResponse {
    private String name;
    private String phoneNumber;
    private String email;
    private String roomNumber;
    private Type roomType;
    private double pricePerNight;
    private double totalPayment;
    private String bookingReferenceNo;
    private String checkInDate;
    private String checkOutDate;
}
