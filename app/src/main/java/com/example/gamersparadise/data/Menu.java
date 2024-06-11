package com.example.gamersparadise.data;

public class Menu {
    private int id;
    private int locationId;
    private int imageResource;
    private String name;
    private String description;
    private int menuTypeId;
    private float price;

    public Menu(int locationId, int imageResource, String name, String description, int menuTypeId, float price) {
        this.locationId = locationId;
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.menuTypeId = menuTypeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(int menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
