package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Promotion implements Parcelable {
    private String id;
    private String name;
    private String code;
    private int promotionTypeId;
    private float nominal;
    private float minimumOrder;
    private String startTime;
    private String endTime;
    private String imageUrl;
    private String description;

    public Promotion() {
    }

    public Promotion(String name, String code, String startTime, String endTime, int promotionTypeId, float nominal, float minimumOrder, String imageUrl, String description) {
        this.name = name;
        this.code = code;
        this.startTime = startTime;
        this.endTime = endTime;
        this.promotionTypeId = promotionTypeId;
        this.nominal = nominal;
        this.minimumOrder = minimumOrder;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTimeAsLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(this.startTime, formatter);
    }

    public void setStartTimeAsLocalDateTime(LocalDateTime startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = startTime.format(formatter);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTimeAsLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(this.endTime, formatter);
    }

    public void setEndTimeAsLocalDateTime(LocalDateTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.endTime = endTime.format(formatter);
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

    public float getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(float minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeSerializable(this.startTime);
        dest.writeSerializable(this.endTime);
        dest.writeInt(this.promotionTypeId);
        dest.writeFloat(this.nominal);
        dest.writeFloat(this.minimumOrder);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.code = source.readString();
        this.startTime = source.readString();
        this.endTime = source.readString();
        this.promotionTypeId = source.readInt();
        this.nominal = source.readFloat();
        this.minimumOrder = source.readFloat();
        this.imageUrl = source.readString();
        this.description = source.readString();
    }

    protected Promotion(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.promotionTypeId = in.readInt();
        this.nominal = in.readFloat();
        this.minimumOrder = in.readFloat();
        this.imageUrl = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Promotion> CREATOR = new Parcelable.Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
}
