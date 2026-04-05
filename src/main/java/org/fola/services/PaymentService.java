package org.fola.services;

import org.fola.dtos.requests.CalculatePaymentRequest;
import org.fola.dtos.responses.CalculatePaymentResponse;
import org.fola.dtos.responses.FestivePeriodPricing;
import org.fola.utils.Mapper;
import org.fola.utils.RoomPricing;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public FestivePeriodPricing viewFestivePeriodPrices(){
        FestivePeriodPricing pricing = new FestivePeriodPricing();
        pricing.setSingleRoom(RoomPricing.SINGLE + (RoomPricing.SINGLE * RoomPricing.FESTIVE_RATE));
        pricing.setDoubleRoom(RoomPricing.DOUBLE + (RoomPricing.DOUBLE * RoomPricing.FESTIVE_RATE));
        pricing.setSuiteRoom(RoomPricing.SUITE + (RoomPricing.SUITE * RoomPricing.FESTIVE_RATE));
        return pricing;
    }

    public CalculatePaymentResponse calculatePayment(CalculatePaymentRequest request){
        CalculatePaymentResponse response = Mapper.map(request);
        if(request.isFestive()) response.setFestivePeriodSurcharge(String.valueOf(RoomPricing.FESTIVE_RATE * 100) + "%");
        if(!request.isFestive()) response.setFestivePeriodSurcharge("0%");

        double pricePerNight = setPriceByRoomType(String.valueOf(request.getRoomType()));
        if(request.isFestive()) pricePerNight += pricePerNight * RoomPricing.FESTIVE_RATE;
        response.setPricePerNight(pricePerNight);
        response.setTotalPayment(pricePerNight * request.getNumberOfNights());

        return response;
    }

    private double setPriceByRoomType(String roomType){
        if(roomType.equalsIgnoreCase("single")) return RoomPricing.SINGLE;
        if(roomType.equalsIgnoreCase("double")) return RoomPricing.DOUBLE;
        if(roomType.equalsIgnoreCase("suite")) return RoomPricing.SUITE;
        return 0;
    }
}
