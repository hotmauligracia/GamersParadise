package com.example.gamersparadise.data;

public class Location {
    private int id;
    private String name;
    private String province;
    private String city;
    private String subdistrict;
    private String zipCode;
    private String street;

    public Location(String name, String province, String city, String subdistrict, String zipCode, String street) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.subdistrict = subdistrict;
        this.zipCode = zipCode;
        this.street = street;
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
}
