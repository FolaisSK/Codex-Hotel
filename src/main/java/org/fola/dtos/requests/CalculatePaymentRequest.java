package org.fola.dtos.requests;

import lombok.Data;
import org.fola.data.models.Type;

@Data
public class CalculatePaymentRequest {
    private Type roomType;
    private int numberOfNights;
    private boolean isFestive;
}
