package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuType implements Parcelable {
    private int id;
    private String name;

    public MenuType() {
    }

    public MenuType(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
    }

    protected MenuType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<MenuType> CREATOR = new Parcelable.Creator<MenuType>() {
        @Override
        public MenuType createFromParcel(Parcel source) {
            return new MenuType(source);
        }

        @Override
        public MenuType[] newArray(int size) {
            return new MenuType[size];
        }
    };
}
