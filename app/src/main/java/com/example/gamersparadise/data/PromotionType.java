package com.example.gamersparadise.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PromotionType implements Parcelable {
    private int id;
    private String name;
    private String operator;

    public PromotionType() {
    }

    public PromotionType(String name, String operator) {
        this.name = name;
        this.operator = operator;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.operator);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.operator = source.readString();
    }

    protected PromotionType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.operator = in.readString();
    }

    public static final Parcelable.Creator<PromotionType> CREATOR = new Parcelable.Creator<PromotionType>() {
        @Override
        public PromotionType createFromParcel(Parcel source) {
            return new PromotionType(source);
        }

        @Override
        public PromotionType[] newArray(int size) {
            return new PromotionType[size];
        }
    };
}
