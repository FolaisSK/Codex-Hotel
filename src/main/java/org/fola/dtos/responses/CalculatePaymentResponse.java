package org.fola.dtos.responses;

import lombok.Data;

@Data
public class CalculatePaymentResponse {
    private String roomType;
    private double pricePerNight;
    private String festivePeriodSurcharge;
    private double totalPayment;
}
