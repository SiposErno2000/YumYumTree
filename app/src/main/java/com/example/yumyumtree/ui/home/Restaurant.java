package com.example.yumyumtree.ui.home;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("area")
    private String area;
    @SerializedName("phone")
    private String phone;
    @SerializedName("image_url")
    private String image_url;

    public Restaurant(int id, String name, String address, String city, String area, String phone, String image_url) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.area = area;
        this.phone = phone;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage_url() {
        return image_url;
    }
}
