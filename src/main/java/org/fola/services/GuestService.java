package org.fola.services;

import org.fola.data.repositories.GuestRepository;
import org.fola.data.repositories.ReservationRepository;
import org.fola.data.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestService {
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ReservationRepository reservationRepository;


}
