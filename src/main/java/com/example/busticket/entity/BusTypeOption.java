package com.example.busticket.entity;

public enum BusTypeOption {
    SEAT_16("Xe 16 chỗ", 16),
    SEAT_29("Xe 29 chỗ", 29),
    SEAT_45("Xe 45 chỗ", 45),
    LIMOUSINE_20("Limousine 20 chỗ", 20),
    SLEEPER_40("Giường nằm 40 chỗ", 40);

    private final String label;
    private final int totalSeats;

    BusTypeOption(String label, int totalSeats) {
        this.label = label;
        this.totalSeats = totalSeats;
    }

    public String getLabel() {
        return label;
    }

    public int getTotalSeats() {
        return totalSeats;
    }
}
