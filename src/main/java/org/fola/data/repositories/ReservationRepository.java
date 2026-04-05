package org.fola.data.repositories;

import org.fola.data.models.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    Optional<Reservation> findByRoomNumber(String roomNumber);

    Optional<Reservation> findByReferenceNo(String referenceNumber);
}
