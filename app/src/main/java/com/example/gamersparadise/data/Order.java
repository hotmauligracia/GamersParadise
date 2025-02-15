package com.example.gamersparadise.data;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Locale;

public class Order {
    private String id;
    private String userId;
    private String reservationId;
    private ArrayList<String> menuId;
    private String promotionId;
    private float totalPrice;
    private String status;
    private String createdAt;
    private String updatedAt;

    public Order(String userId, String reservationId, ArrayList<String> menuId, String promotionId, float totalPrice, String status, String createdAt, String updatedAt) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.menuId = menuId;
        this.promotionId = promotionId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public ArrayList<String> getMenuId() {
        return menuId;
    }

    public void setMenuId(ArrayList<String> menuId) {
        this.menuId = menuId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFormattedTotalPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(totalPrice);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
