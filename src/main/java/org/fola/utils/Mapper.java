package org.fola.utils;

import org.fola.data.models.Room;
import org.fola.dtos.requests.AddRoomRequest;
import org.fola.dtos.responses.AddRoomResponse;

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
}
