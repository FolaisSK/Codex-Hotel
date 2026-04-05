package org.fola.utils;

import org.fola.data.models.Reservation;
import org.fola.data.models.Room;
import org.fola.dtos.requests.AddRoomRequest;
import org.fola.dtos.requests.BookRoomRequest;
import org.fola.dtos.requests.CalculatePaymentRequest;
import org.fola.dtos.responses.AddRoomResponse;
import org.fola.dtos.responses.BookRoomResponse;
import org.fola.dtos.responses.CalculatePaymentResponse;

public class Mapper {
    public static Room map(AddRoomRequest request){
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        return room;
    }

    public static AddRoomResponse map(Room room){
        AddRoomResponse response = new AddRoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setRoomType(room.getRoomType());
        response.setAvailable(room.isAvailable());
        response.setPricePerNight(room.getPricePerNight());
        response.setUnderMaintenance(room.isUnderMaintenance());
        return response;
    }

    public static BookRoomResponse map(BookRoomRequest request, Reservation reservation){
        BookRoomResponse response = new BookRoomResponse();
        response.setName(request.getName());
        response.setEmail(request.getEmail());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setCheckOutDate(String.valueOf(reservation.getCheckOutDate()));
        response.setCheckInDate(String.valueOf(reservation.getCheckInDate()));
        response.setRoomType(request.getRoomType());
        response.setRoomNumber(reservation.getRoomNumber());
        response.setBookingReferenceNo(reservation.getReferenceNo());
        response.setPricePerNight(reservation.getPricePerNight());
        response.setTotalPayment(reservation.getTotalPayment());

        return response;
    }

    public static CalculatePaymentResponse map(CalculatePaymentRequest request){
        CalculatePaymentResponse response = new CalculatePaymentResponse();
        response.setRoomType(response.getRoomType());
        return response;
    }
}
