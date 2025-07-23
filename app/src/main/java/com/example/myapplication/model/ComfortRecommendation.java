package com.example.myapplication.model;

public class ComfortRecommendation {
    public int id;
    public String timestamp;
    public String roomName;
    public String recommendation;

    public String getRecommendation() {
        return recommendation;
    }

    // (необов'язково) інші геттери:
    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRoomName() {
        return roomName;
    }
}
