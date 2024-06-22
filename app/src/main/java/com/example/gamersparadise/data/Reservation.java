package com.example.gamersparadise.data;

import java.text.NumberFormat;

import java.util.Locale;

public class Reservation {
    private String id;
    private String userId;
    private String facilityId;
    private String customerName;
    private String customerPhone;
    private String reservationTime;
    private String promotionId;
    private float totalPrice;
    private boolean isLate;
    private String status;
    private String createdAt;
    private String updatedAt;

    public Reservation(String userId, String facilityId, String customerName, String customerPhone, String reservationTime, String promotionId, float totalPrice, boolean isLate, String status, String createdAt, String updatedAt) {
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

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
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
