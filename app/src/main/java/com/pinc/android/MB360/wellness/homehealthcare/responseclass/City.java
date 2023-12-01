package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City implements Parcelable {

    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Is_metro")
    @Expose
    private String isMetro;
    @SerializedName("Srno")
    @Expose
    private String srno;

    protected City(Parcel in) {
        city = in.readString();
        isMetro = in.readString();
        srno = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsMetro() {
        return isMetro;
    }

    public void setIsMetro(String isMetro) {
        this.isMetro = isMetro;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", isMetro='" + isMetro + '\'' +
                ", srno='" + srno + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(isMetro);
        parcel.writeString(srno);
    }
}
