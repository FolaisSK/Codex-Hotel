package org.fola.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Room {
    @Id
    private String id;
    private String roomNumber;
    private Type roomType;
    private double pricePerNight;
    private boolean isAvailable = true;
    private boolean isUnderMaintenance;
}
