package com.example.minipetfinder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets")
public class PetEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String breed;
    private String size;
    private String name;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    // Constructor completo
    public PetEntity(String breed, String size, String name, String phoneNumber, double latitude, double longitude) {
        this.breed = breed;
        this.size = size;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructor vacío para Room
    public PetEntity() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
