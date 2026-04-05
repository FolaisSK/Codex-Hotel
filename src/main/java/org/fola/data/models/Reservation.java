package org.fola.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Reservation {
    @Id
    private String id;
    private String roomNumber;
    private Type roomType;
    private double pricePerNight;
    private double totalPayment;
    private String referenceNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Guest guest;
}
