package com.klef.dev.controller;

import com.klef.dev.entity.Room;
import com.klef.dev.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotelapi")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @GetMapping("/")
    public String base() {
        return "My basic Project is running";
    }
    @GetMapping("/all")
    public List<Room> all() {
        return roomService.getAllRooms();
    }

    @GetMapping("/get/{id}")
    public Room one(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Room> add(@RequestBody Room room) {
        Room saved = roomService.addRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/update")
    public Room update(@RequestBody Room room) {
        return roomService.updateRoom(room);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.ok("Deleted " + id);
    }
}
