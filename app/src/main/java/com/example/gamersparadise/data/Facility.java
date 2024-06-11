package com.example.gamersparadise.data;

public class Facility {
    private int id;
    private int locationId;
    private int imageResource;
    private String name;
    private int capacity;
    private String details;
    private float price;

    public Facility(int locationId, int imageResource, String name, int capacity, String details, float price) {
        this.locationId = locationId;
        this.imageResource = imageResource;
        this.name = name;
        this.capacity = capacity;
        this.details = details;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
