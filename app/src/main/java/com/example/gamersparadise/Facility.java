package com.example.gamersparadise;

public class Facility {
    private int facility_id;
    private int imageResource;
    private String name;
    private int capacity;
    private String equipment;
    private float price;

    public Facility(int imageResource, String name, int capacity, String equipment, float price) {
        this.imageResource = imageResource;
        this.name = name;
        this.capacity = capacity;
        this.equipment = equipment;
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getEquipment() {
        return equipment;
    }

    public float getPrice() {
        return price;
    }
}
