package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.Locale;

public class Menu implements Parcelable {
    private String id;
    private String locationId;
    private String imageUrl;
    private String name;
    private String description;
    private int menuTypeId;
    private float price;
    private boolean isInStock;

    public Menu() {
    }

    public Menu(String locationId, String imageUrl, String name, String description, int menuTypeId, float price, boolean isInStock) {
        this.locationId = locationId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.menuTypeId = menuTypeId;
        this.price = price;
        this.isInStock = isInStock;
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

    public String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(this.price);
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
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
        dest.writeString(this.description);
        dest.writeInt(this.menuTypeId);
        dest.writeFloat(this.price);
        dest.writeByte(this.isInStock ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.locationId = source.readString();
        this.imageUrl = source.readString();
        this.name = source.readString();
        this.description = source.readString();
        this.menuTypeId = source.readInt();
        this.price = source.readFloat();
        this.isInStock = source.readByte() != 0;
    }

    protected Menu(Parcel in) {
        this.id = in.readString();
        this.locationId = in.readString();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.menuTypeId = in.readInt();
        this.price = in.readFloat();
        this.isInStock = in.readByte() != 0;
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel source) {
            return new Menu(source);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
}
