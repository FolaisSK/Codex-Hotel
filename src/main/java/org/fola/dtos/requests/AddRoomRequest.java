package org.fola.dtos.requests;

import lombok.Data;
import org.fola.data.models.Type;

@Data
public class AddRoomRequest {
    private int roomNumber;
    private Type roomType;
}
