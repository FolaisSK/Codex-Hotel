package org.fola.dtos.responses;

import lombok.Data;
import org.fola.data.models.Type;

import java.time.LocalDateTime;

@Data
public class AddRoomResponse {
    private String id;
    private String roomNumber;
    private Type roomType;
    private double pricePerNight;
    private boolean isAvailable;
    private boolean isUnderMaintenance;
    private LocalDateTime createdAt = LocalDateTime.now();
}
