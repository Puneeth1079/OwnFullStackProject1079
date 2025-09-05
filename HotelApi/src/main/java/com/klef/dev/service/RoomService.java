package com.klef.dev.service;

import java.util.List;
import com.klef.dev.entity.Room;

public interface RoomService
{
  List<Room> getAllRooms();
  Room getRoomById(Long id);
  Room addRoom(Room r);
  Room updateRoom(Room r);
  void deleteRoom(Long id);
}
