package org.fola.data.repositories;

import org.fola.data.models.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends MongoRepository<Guest, String> {
    Optional<Guest> findByEmail(String email);
}
