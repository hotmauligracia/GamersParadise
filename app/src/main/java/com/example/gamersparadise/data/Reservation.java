package com.example.gamersparadise.data;

import java.time.LocalDateTime;

public class Reservation {
    private String id;
    private String userId;
    private String facilityId;
    private String customerName;
    private String customerPhone;
    private LocalDateTime reservationTime;
    private String promotionId;
    private String totalPrice;
    private boolean isLate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Reservation(String userId, String facilityId, String customerName, String customerPhone, LocalDateTime reservationTime, String promotionId, String totalPrice, boolean isLate, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.facilityId = facilityId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.reservationTime = reservationTime;
        this.promotionId = promotionId;
        this.totalPrice = totalPrice;
        this.isLate = isLate;
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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
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

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
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
