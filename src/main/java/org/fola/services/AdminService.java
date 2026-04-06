package org.fola.services;

import org.fola.data.models.Reservation;
import org.fola.data.models.Room;
import org.fola.data.models.Type;
import org.fola.data.repositories.AdminRepository;
import org.fola.data.repositories.GuestRepository;
import org.fola.data.repositories.ReservationRepository;
import org.fola.data.repositories.RoomRepository;
import org.fola.dtos.requests.AddRoomRequest;
import org.fola.dtos.responses.*;
import org.fola.exceptions.*;
import org.fola.utils.Mapper;
import org.fola.utils.RoomPricing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ReservationRepository reservationRepository;

    public AddRoomResponse addRoom(AddRoomRequest request){
        if(request.getRoomNumber().isBlank()) throw new InvalidRoomNumberException("Room Number cannot be blank!");
        if(roomRepository.existsByRoomNumber(request.getRoomNumber())) throw new RoomAlreadyExistsException("Room " + request.getRoomNumber() + " already exists!");
        Room room = Mapper.map(request);
        room.setPricePerNight(assignRoomPrice(request.getRoomType()));
        roomRepository.save(room);
        return Mapper.map(room);
    }

    public List<AddRoomResponse> viewAllRooms(){
        return roomRepository.findAll().stream().map(room -> Mapper.map(room)).toList();
    }

    public AddRoomResponse markRoomForMaintenance(String roomNumber){
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()-> new RoomDoesNotExistException("Room does not exist!"));
        if(room.isUnderMaintenance()) throw  new RoomIsAlreadyUnavailable("Room " + roomNumber +" is already under maintenance!");
        room.setAvailable(false);
        room.setUnderMaintenance(true);
        roomRepository.save(room);
        return Mapper.map(room);
    }

    public AddRoomResponse unmarkRoomForMaintenance(String roomNumber){
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()-> new RoomDoesNotExistException("Room does not exist!"));
        if(!room.isUnderMaintenance()) throw new RoomIsAlreadyAvailable("Room " + roomNumber +" is not under maintenance!");
        room.setAvailable(true);
        room.setUnderMaintenance(false);
        roomRepository.save(room);
        return Mapper.map(room);
    }

    public ViewGuestDetailsResponse viewGuestDetails(String roomNumber){
        Reservation reservation = reservationRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()-> new ReservationDoesNotExist("No Reservation for Room" + roomNumber + "."));
        ViewGuestDetailsResponse response = new ViewGuestDetailsResponse();
        response.setName(reservation.getGuest().getName());
        response.setEmail(reservation.getGuest().getEmail());
        response.setPhoneNumber(reservation.getGuest().getPhoneNumber());
        response.setReferenceNo(reservation.getReferenceNo());
        response.setRoomNumber(reservation.getRoomNumber());
        response.setCheckInDate(String.valueOf(reservation.getCheckInDate()));
        response.setCheckOutDate(String.valueOf(reservation.getCheckOutDate()));
        return response;
    }


    private double assignRoomPrice(Type roomType){
        if(roomType == Type.SINGLE) return RoomPricing.SINGLE;
        if(roomType == Type.DOUBLE) return RoomPricing.DOUBLE;
        if(roomType == Type.SUITE) return RoomPricing.SUITE;
        return 0;
    }
}
