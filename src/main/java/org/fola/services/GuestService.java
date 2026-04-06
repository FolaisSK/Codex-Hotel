package org.fola.services;

import org.fola.data.models.Guest;
import org.fola.data.models.Reservation;
import org.fola.data.models.Room;
import org.fola.data.repositories.GuestRepository;
import org.fola.data.repositories.ReservationRepository;
import org.fola.data.repositories.RoomRepository;
import org.fola.dtos.requests.BookRoomRequest;
import org.fola.dtos.responses.AddRoomResponse;
import org.fola.dtos.responses.BookRoomResponse;
import org.fola.exceptions.*;
import org.fola.utils.Mapper;
import org.fola.utils.ReferenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GuestService {
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @Transactional
    public BookRoomResponse bookRoom(BookRoomRequest request){
        if (request.getNoOfNights() < 1) throw new InvalidNumberOfNightsException("Number of Nights must be 1 or more!");
        Room foundRoom = null;
        boolean isFound = false;
        for(Room room : roomRepository.findAll()){
            if(room.isAvailable() && room.getRoomType() == request.getRoomType()) {foundRoom = room;isFound = true;break;}
        }
        if(!isFound) throw new RoomTypeIsNotAvailable("Room of type: " + request.getRoomType() + " is not available!");

        foundRoom.setAvailable(false);
        roomRepository.save(foundRoom);


        Reservation reservation = new Reservation();

        Guest guest = new Guest();
        guest.setEmail(request.getEmail());
        guest.setName(request.getName());
        guest.setPhoneNumber(request.getPhoneNumber());
        guestRepository.save(guest);

        reservation.setGuest(guest);

        reservation.setRoomNumber(foundRoom.getRoomNumber());
        reservation.setPricePerNight(foundRoom.getPricePerNight());
        reservation.setReferenceNo(generateRefNo());
        reservation.setRoomType(foundRoom.getRoomType());
        reservation.setTotalPayment(foundRoom.getPricePerNight() * request.getNoOfNights());
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(request.getNoOfNights()));
        reservationRepository.save(reservation);

        return Mapper.map(request, reservation);
    }

    @Transactional
    public String cancelReservation(String referenceNumber){
        Reservation reservation = reservationRepository.findByReferenceNo(referenceNumber)
                .orElseThrow(()-> new ReservationDoesNotExist("Reservation does not exist!"));
        if(!reservation.isValid()) throw new ReservationIsInvalidException("Reservation is invalid!");
        reservation.setValid(false);
        reservationRepository.save(reservation);

        Room room = roomRepository.findByRoomNumber(reservation.getRoomNumber())
                .orElseThrow(()-> new RoomDoesNotExistException("Room does not exist!"));
        room.setAvailable(true);
        roomRepository.save(room);

        return "Reservation canceled successfully.\nRoom number " + reservation.getRoomNumber() + " is now available.";
    }

    public Reservation viewBooking (String referenceNo){
        return reservationRepository.findByReferenceNo(referenceNo).orElseThrow(()-> new ReservationDoesNotExist("Reservation does not exist!"));
    }

    public List<AddRoomResponse> viewAllAvailableRooms (){
        List<AddRoomResponse> availableRooms = new ArrayList<>();
        for(Room room : roomRepository.findAll()){
            if(room.isAvailable()) availableRooms.add(Mapper.map(room));
        }
        return availableRooms;
    }

    public String checkRoomAvailability(String roomNumber){
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()-> new RoomDoesNotExistException("Room does not exist!"));
        if(room.isAvailable()) return "Room " + roomNumber + " is available!";
        return "Room is not available!";
    }

    private String generateRefNo(){
        return ReferenceNumberGenerator.generateRefNo(4);
    }
}
