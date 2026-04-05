package org.fola.services;

import org.fola.data.models.Guest;
import org.fola.data.models.Reservation;
import org.fola.data.models.Room;
import org.fola.data.models.Type;
import org.fola.data.repositories.AdminRepository;
import org.fola.data.repositories.GuestRepository;
import org.fola.data.repositories.ReservationRepository;
import org.fola.data.repositories.RoomRepository;
import org.fola.dtos.requests.AddRoomRequest;
import org.fola.dtos.requests.BookRoomRequest;
import org.fola.dtos.requests.CalculatePaymentRequest;
import org.fola.dtos.responses.*;
import org.fola.exceptions.*;
import org.fola.utils.Mapper;
import org.fola.utils.ReferenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private static final double singleRoomPrice = 10_000;
    private static final double doubleRoomPrice = 15_000;
    private static final double suiteRoomPrice = 25_000;
    private static final double festiveRate = 0.2;

    public AddRoomResponse addRoom(AddRoomRequest request){
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

    public BookRoomResponse bookRoom(BookRoomRequest request){
        Room foundRoom = null;
        boolean isFound = false;
        for(Room room : roomRepository.findAll()){
            if(room.isAvailable() && room.getRoomType() == request.getRoomType()) {foundRoom = room;isFound = true;break;}
        }
        if(!isFound) throw new RoomTypeIsNotAvailable("Room is not available!");

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

    public String cancelReservation(String referenceNumber){
        Reservation reservation = reservationRepository.findByReferenceNo(referenceNumber)
                .orElseThrow(()-> new ReservationDoesNotExist("Reservation does not exist!"));
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

    public FestivePeriodPricing viewFestivePeriodPrices(){
        FestivePeriodPricing pricing = new FestivePeriodPricing();
        pricing.setSingleRoom(singleRoomPrice + (singleRoomPrice * festiveRate));
        pricing.setDoubleRoom(doubleRoomPrice + (doubleRoomPrice * festiveRate));
        pricing.setSuiteRoom(suiteRoomPrice + (suiteRoomPrice * festiveRate));
        return pricing;
    }

    public CalculatePaymentResponse calculatePayment(CalculatePaymentRequest request){
        CalculatePaymentResponse response = Mapper.map(request);
        if(request.isFestive()) response.setFestivePeriodSurcharge(String.valueOf(festiveRate * 100) + "%");
        if(!request.isFestive()) response.setFestivePeriodSurcharge("0%");

        double pricePerNight = setPriceByRoomType(String.valueOf(request.getRoomType()));
        response.setPricePerNight(pricePerNight);
        response.setTotalPayment(pricePerNight * request.getNumberOfNights());

        return response;
    }

    private double setPriceByRoomType(String roomType){
        if(roomType.equalsIgnoreCase("single")) return singleRoomPrice;
        if(roomType.equalsIgnoreCase("double")) return doubleRoomPrice;
        if(roomType.equalsIgnoreCase("suite")) return suiteRoomPrice;
        return 0;
    }

    private String generateRefNo(){
        return ReferenceNumberGenerator.generateRefNo(4);
    }

    private double assignRoomPrice(Type roomType){
        if(roomType == Type.SINGLE) return singleRoomPrice;
        if(roomType == Type.DOUBLE) return doubleRoomPrice;
        if(roomType == Type.SUITE) return suiteRoomPrice;
        return 0;
    }
}
