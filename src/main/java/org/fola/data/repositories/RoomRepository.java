package org.fola.data.repositories;

import org.fola.data.models.Room;
import org.fola.data.models.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

    Optional<Room> findByRoomNumber(String roomNumber);

    Optional<Room> findByRoomType(Type roomType);
}
