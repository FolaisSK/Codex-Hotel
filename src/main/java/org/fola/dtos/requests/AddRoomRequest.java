package org.fola.dtos.requests;

import lombok.Data;
import org.fola.data.models.Type;

@Data
public class AddRoomRequest {
    private String roomNumber;
    private Type roomType;
}
