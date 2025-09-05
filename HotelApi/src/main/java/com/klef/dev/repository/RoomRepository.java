package com.klef.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klef.dev.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>
{ 
}
