package com.klef.dev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_table")
public class Room
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "room_id")
  private Long id;

  @Column(name = "room_name", nullable = false, length = 50)
  private String name;

  @Column(name = "room_type", nullable = false, length = 20)     // SINGLE | DOUBLE | SUITE
  private String type;

  @Column(name = "room_status", nullable = false, length = 20)   // AVAILABLE | OCCUPIED | MAINTENANCE
  private String status;

  @Column(name = "room_price", nullable = false)
  private Double pricePerNight;

  @Column(name = "room_capacity", nullable = false)
  private Integer capacity;

  @Column(name = "room_floor", nullable = false, length = 10)
  private String floor;

  @Column(name = "room_amenities", length = 200)
  private String amenities;

  @Column(name = "room_description", length = 500)
  private String description;

  // Getters & Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public Double getPricePerNight() { return pricePerNight; }
  public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }

  public Integer getCapacity() { return capacity; }
  public void setCapacity(Integer capacity) { this.capacity = capacity; }

  public String getFloor() { return floor; }
  public void setFloor(String floor) { this.floor = floor; }

  public String getAmenities() { return amenities; }
  public void setAmenities(String amenities) { this.amenities = amenities; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  @Override
  public String toString() {
    return "Room [id=" + id + ", name=" + name + ", type=" + type + ", status=" + status +
           ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + ", floor=" + floor +
           ", amenities=" + amenities + ", description=" + description + "]";
  }
}
