package com.example.myapplication.model;

public class SensorDataDto {
    private String roomName;
    private Double temperatureDht;
    private Double humidityDht;
    private String gasDetected;
    private Double pressure;
    private Double altitude;
    private String timestamp;

    public String getRoomName() {
        return roomName;
    }

    public Double getTemperatureDht() {
        return temperatureDht;
    }

    public Double getHumidityDht() {
        return humidityDht;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getAltitude() {
        return altitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getGasDetected() {
        return gasDetected;
    }
}
