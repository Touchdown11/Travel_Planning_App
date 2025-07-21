package com.example.travelplanningapp;

public class Itinerary {
    private String destination;
    private String date;
    private String activity;

    // Empty constructor for Firestore
    public Itinerary() {}

    // Constructor to create itinerary
    public Itinerary(String destination, String date, String activity) {
        this.destination = destination;
        this.date = date;
        this.activity = activity;
    }

    // Getters and setters
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
