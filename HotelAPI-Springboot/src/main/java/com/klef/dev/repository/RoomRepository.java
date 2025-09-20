package com.klef.dev.repository;

import com.klef.dev.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> 
{
}
