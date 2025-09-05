package com.klef.dev.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.dev.entity.Room;
import com.klef.dev.service.RoomService;

@RestController
@RequestMapping("/roomapi")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController
{
  private final RoomService service;

  public RoomController(RoomService service) {
    this.service = service;
  }

  @GetMapping("/all")
  public List<Room> all() {
    return service.getAllRooms();
  }

  // IMPORTANT: include the variable name "id"
  @GetMapping("/get/{id}")
  public Room get(@PathVariable("id") Long id) {
    return service.getRoomById(id);
  }

  
  @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
  public Room add(@RequestBody Room r) {
    return service.addRoom(r);
  }

  @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
  public Room update(@RequestBody Room r) {
    return service.updateRoom(r);
  }

  // IMPORTANT: include the variable name "id"
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    service.deleteRoom(id);
    return ResponseEntity.ok("Deleted");
  }

  // TEMP: allow browser GET for delete testing
  @GetMapping("/delete/{id}")
  public ResponseEntity<String> deleteViaGet(@PathVariable("id") Long id) {
    return delete(id);
  }


  // --- Optional: nice error messages instead of Whitelabel ---

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(java.util.NoSuchElementException.class)
  public ResponseEntity<String> handleNotFound(java.util.NoSuchElementException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
