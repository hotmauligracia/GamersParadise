package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PromotionCategory implements Parcelable {
    private int id;
    private String name;

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

    public PromotionCategory() {
    }

    protected PromotionCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<PromotionCategory> CREATOR = new Parcelable.Creator<PromotionCategory>() {
        @Override
        public PromotionCategory createFromParcel(Parcel source) {
            return new PromotionCategory(source);
        }

        @Override
        public PromotionCategory[] newArray(int size) {
            return new PromotionCategory[size];
        }
    };
}
