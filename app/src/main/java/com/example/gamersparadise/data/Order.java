package com.example.gamersparadise.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private String id;
    private String userId;
    private String reservationId;
    private ArrayList<String> menuId;
    private String promotionId;
    private String totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String userId, String reservationId, ArrayList<String> menuId, String promotionId, String totalPrice, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
