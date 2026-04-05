package org.fola.dtos.responses;

import lombok.Data;

@Data
public class ViewGuestDetailsResponse {
    private String name;
    private String phoneNumber;
    private String email;
    private String roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private String referenceNo;
}
