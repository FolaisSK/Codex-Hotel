package org.fola.dtos.responses;

import lombok.Data;
import org.fola.data.models.Type;

@Data
public class CalculatePaymentResponse {
    private Type roomType;
    private double pricePerNight;
    private String festivePeriodSurcharge;
    private double totalPayment;
}
