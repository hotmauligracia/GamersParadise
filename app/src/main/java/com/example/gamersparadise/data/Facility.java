package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.Locale;

public class Facility implements Parcelable {
    private String id;
    private String locationId;
    private String imageUrl;
    private String name;
    private int capacity;
    private String details;
    private float price;

    public Facility() {
    }

    public Facility(String locationId, String imageUrl, String name, int capacity, String details, float price) {
        this.locationId = locationId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.capacity = capacity;
        this.details = details;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(this.price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.locationId);
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeInt(this.capacity);
        dest.writeString(this.details);
        dest.writeFloat(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.locationId = source.readString();
        this.imageUrl = source.readString();
        this.name = source.readString();
        this.capacity = source.readInt();
        this.details = source.readString();
        this.price = source.readFloat();
    }

    protected Facility(Parcel in) {
        this.id = in.readString();
        this.locationId = in.readString();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.capacity = in.readInt();
        this.details = in.readString();
        this.price = in.readFloat();
    }

    public static final Parcelable.Creator<Facility> CREATOR = new Parcelable.Creator<Facility>() {
        @Override
        public Facility createFromParcel(Parcel source) {
            return new Facility(source);
        }

        @Override
        public Facility[] newArray(int size) {
            return new Facility[size];
        }
    };
}
