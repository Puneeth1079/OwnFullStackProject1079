package com.klef.dev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_table")
public class Room {
    @Id
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name", nullable = false, length = 80)
    private String name;

    // e.g., SINGLE / DOUBLE / SUITE
    @Column(name = "room_type", nullable = false, length = 20)
    private String type;

    // e.g., AVAILABLE / BOOKED / MAINTENANCE
    @Column(name = "room_status", nullable = false, length = 20)
    private String status;

    // numeric; allow null -> treat as 0 or show validation in UI if needed
    @Column(name = "room_price_per_night")
    private Double pricePerNight;

    @Column(name = "room_capacity")
    private Integer capacity;

    @Column(name = "room_floor")
    private Integer floor;

    // comma-separated list, e.g., "AC,WiFi,TV"
    @Column(name = "room_amenities", length = 255)
    private String amenities;

    @Column(name = "room_description", length = 2000)
    private String description;

    // --- Getters & Setters ---
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

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Room [id=" + id
                + ", name=" + name
                + ", type=" + type
                + ", status=" + status
                + ", pricePerNight=" + pricePerNight
                + ", capacity=" + capacity
                + ", floor=" + floor
                + ", amenities=" + amenities
                + ", description=" + description + "]";
    }
}
