package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Promotion implements Parcelable {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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
        return LocalDateTime.parse(this.startTime, dateTimeFormatter);
    }

    public void setStartTimeAsLocalDateTime(LocalDateTime startTime) {
        this.startTime = startTime.format(dateTimeFormatter);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTimeAsLocalDateTime() {
        return LocalDateTime.parse(this.endTime, dateTimeFormatter);
    }

    public void setEndTimeAsLocalDateTime(LocalDateTime endTime) {
        this.endTime = endTime.format(dateTimeFormatter);
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

    public String getFormattedNominal() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(this.nominal);
    }

    public float getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(float minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public String getFormattedMinimumOrder() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(this.minimumOrder);
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
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
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
