package com.klef.dev.service;

import java.util.List;
import com.klef.dev.entity.Room;

public interface RoomService {
    Room addRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room updateRoom(Room room);
    void deleteRoomById(Long id);
}
