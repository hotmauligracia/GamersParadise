package com.example.gamersparadise.data;

import com.google.type.DateTime;

public class Promotion {
    private int id;
    private String name;
    private DateTime startTime;
    private DateTime endTime;
    private int imageResource;
    private int promotionTypeId;
    private float nominal;

    public Promotion(String name, DateTime startTime, DateTime endTime, int imageResource, int promotionTypeId, float nominal) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageResource = imageResource;
        this.promotionTypeId = promotionTypeId;
        this.nominal = nominal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getPromotionTypeId() {
        return promotionTypeId;
    }

    public void setPromotionTypeId(int promotionTypeId) {
        this.promotionTypeId = promotionTypeId;
    }

    public float getNominal() {
        return nominal;
    }

    public void setNominal(float nominal) {
        this.nominal = nominal;
    }
}
