package org.fola.dtos.requests;

import lombok.Data;
import org.fola.data.models.Type;

@Data
public class BookRoomRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private Type roomType;
    private int noOfNights;
}
