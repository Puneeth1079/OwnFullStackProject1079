package com.klef.dev.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.klef.dev.entity.Room;
import com.klef.dev.repository.RoomRepository;

@Service
@Transactional
public class RoomServiceImpl implements RoomService
{
  private final RoomRepository repo;

  public RoomServiceImpl(RoomRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Room> getAllRooms() {
    return repo.findAll();
  }

  @Override
  public Room getRoomById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Room not found"));
  }

  @Override
  public Room addRoom(Room r) {
    basicValidate(r);
    return repo.save(r);
  }

  @Override
  public Room updateRoom(Room r) {
    if (r.getId() == null) throw new IllegalArgumentException("id is required");
    basicValidate(r);
    Room existing = getRoomById(r.getId());
    existing.setName(r.getName());
    existing.setType(r.getType());
    existing.setStatus(r.getStatus());
    existing.setPricePerNight(r.getPricePerNight());
    existing.setCapacity(r.getCapacity());
    existing.setFloor(r.getFloor());
    existing.setAmenities(r.getAmenities());
    existing.setDescription(r.getDescription());
    return existing; // saved on transaction commit
  }

  @Override
  public void deleteRoom(Long id) {
    repo.deleteById(id);
  }

  // Minimal manual checks since you don't use validation annotations
  private void basicValidate(Room r) {
    if (r.getName() == null || r.getName().trim().isEmpty())
      throw new IllegalArgumentException("name is required");
    if (r.getType() == null || r.getType().trim().isEmpty())
      throw new IllegalArgumentException("type is required");
    if (r.getStatus() == null || r.getStatus().trim().isEmpty())
      throw new IllegalArgumentException("status is required");
    if (r.getPricePerNight() == null || r.getPricePerNight() < 0)
      throw new IllegalArgumentException("pricePerNight must be >= 0");
    if (r.getCapacity() == null || r.getCapacity() <= 0)
      throw new IllegalArgumentException("capacity must be > 0");
    if (r.getFloor() == null || r.getFloor().trim().isEmpty())
      throw new IllegalArgumentException("floor is required");
  }
}
