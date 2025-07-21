package com.example.travelplanningapp;

public class Accommodation {
    private String name;
    private String location;
    private double price;
    private String amenities;

    public Accommodation() {
        // Default constructor
    }

    public Accommodation(String name, String location, double price, String amenities) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.amenities = amenities;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}