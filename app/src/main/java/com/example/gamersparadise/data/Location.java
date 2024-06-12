package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    private String id;
    private String name;
    private String province;
    private String city;
    private String subdistrict;
    private String zipCode;
    private String street;

    public Location() {
    }

    public Location(String name, String province, String city, String subdistrict, String zipCode, String street) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.subdistrict = subdistrict;
        this.zipCode = zipCode;
        this.street = street;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.subdistrict);
        dest.writeString(this.zipCode);
        dest.writeString(this.street);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.province = source.readString();
        this.city = source.readString();
        this.subdistrict = source.readString();
        this.zipCode = source.readString();
        this.street = source.readString();
    }

    protected Location(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.subdistrict = in.readString();
        this.zipCode = in.readString();
        this.street = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
