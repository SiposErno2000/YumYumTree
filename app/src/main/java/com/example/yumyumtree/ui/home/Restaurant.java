package com.example.yumyumtree.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Restaurant implements Parcelable {

    @SerializedName("id")
    private final int id;
    @SerializedName("name")
    private final String name;
    @SerializedName("address")
    private final String address;
    @SerializedName("city")
    private final String city;
    @SerializedName("area")
    private final String area;
    @SerializedName("phone")
    private final String phone;
    @SerializedName("image_url")
    private final String image_url;
    @SerializedName("reserve_url")
    private final String reserve_url;

    public Restaurant(int id, String name, String address, String city, String area, String phone, String image_url, String reserve_url) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.area = area;
        this.phone = phone;
        this.image_url = image_url;
        this.reserve_url = reserve_url;
    }

    protected Restaurant(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        area = in.readString();
        phone = in.readString();
        image_url = in.readString();
        reserve_url = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public String getReserve_url() {
        return reserve_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeString(phone);
        dest.writeString(image_url);
        dest.writeString(reserve_url);
    }
}
